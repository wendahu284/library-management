// 公共工具函数

// 渲染导航栏右侧（根据登录状态）
function renderNav() {
    const user = JSON.parse(localStorage.getItem('user') || 'null');
    const el = document.getElementById('navRight');
    if (!el) return;
    if (user) {
        el.innerHTML = `
            <a href="/client/my-borrows" class="btn btn-outline-light btn-sm me-2">我的借阅</a>
            <a href="/client/profile" class="btn btn-outline-light btn-sm me-2">个人中心</a>
            <button class="btn btn-outline-light btn-sm" onclick="doLogout()">退出</button>`;
    } else {
        el.innerHTML = `
            <a href="/login" class="btn btn-outline-light btn-sm me-2">登录</a>
            <a href="/client/register" class="btn btn-outline-light btn-sm">注册</a>`;
    }
}

// 退出登录
function doLogout() {
    fetch('/api/auth/logout', { method: 'POST' }).finally(() => {
        localStorage.removeItem('user');
        location.href = '/';
    });
}

// =====================================================
// 图书封面工具函数
// =====================================================

// 封面颜色方案（根据分类ID或书名哈希选择）
const COVER_COLORS = [
    ['#667eea', '#764ba2'], // 紫色
    ['#f093fb', '#f5576c'], // 粉红
    ['#4facfe', '#00f2fe'], // 蓝色
    ['#43e97b', '#38f9d7'], // 绿色
    ['#fa709a', '#fee140'], // 橙色
    ['#a18cd1', '#fbc2eb'], // 淡紫
    ['#fccb90', '#d57eeb'], // 暖紫
    ['#e0c3fc', '#8ec5fc'], // 浅蓝紫
    ['#f5576c', '#ff6f00'], // 红橙
    ['#667eea', '#4facfe'], // 蓝紫
];

/**
 * 根据字符串生成稳定的颜色索引
 */
function hashColor(str) {
    let hash = 0;
    for (let i = 0; i < str.length; i++) {
        hash = ((hash << 5) - hash) + str.charCodeAt(i);
        hash |= 0;
    }
    return Math.abs(hash) % COVER_COLORS.length;
}

/**
 * 获取图书封面图片URL
 * @param {Object} book - 图书对象 {coverUrl, isbn, title}
 * @param {string} size - 'S' 小图, 'M' 中图, 'L' 大图
 */
function getCoverUrl(book, size) {
    size = size || 'M';
    // 1. 如果已有封面URL，直接使用
    if (book.coverUrl && book.coverUrl.trim()) {
        return book.coverUrl.trim();
    }
    // 2. 尝试通过ISBN从Open Library获取封面
    if (book.isbn) {
        return 'https://covers.openlibrary.org/b/isbn/' + book.isbn + '-' + size + '.jpg';
    }
    // 3. 无ISBN则返回空，由 getCoverHtml 生成占位图
    return '';
}

/**
 * 生成图书封面HTML（带图片加载失败回退）
 * @param {Object} book - 图书对象
 * @param {string} size - 'small'(首页卡片), 'medium'(管理列表), 'large'(详情页)
 */
function getCoverHtml(book, size) {
    var coverUrl = getCoverUrl(book, size === 'large' ? 'L' : 'M');
    var title = book.title || '?';
    var firstChar = title.charAt(0);
    var colors = COVER_COLORS[hashColor(title)];
    var sizes = {
        small:  { height: '200px', icon: '3rem', text: '1.2rem' },
        medium: { height: '60px',  icon: '1.2rem', text: '0.9rem', width: '45px' },
        large:  { height: '400px', icon: '6rem', text: '3rem' }
    };
    var s = sizes[size] || sizes.small;

    if (coverUrl) {
        // 有封面URL，显示图片 + 加载失败回退到占位符
        var fallbackHtml = '<div style="height:' + s.height + ';width:' + (s.width||'100%') +
            ';background:linear-gradient(135deg,' + colors[0] + ',' + colors[1] +
            ');display:flex;align-items:center;justify-content:center;border-radius:4px;">' +
            '<span style="font-size:' + s.text + ';color:#fff;font-weight:bold;">' +
            firstChar + '</span></div>';
        return '<div style="height:' + s.height + ';width:' + (s.width||'100%') +
            ';overflow:hidden;border-radius:4px;background:#f0f0f0;">' +
            '<img src="' + coverUrl + '" alt="' + title + '" ' +
            'style="width:100%;height:100%;object-fit:cover;" ' +
            'onerror="this.style.display=\'none\';this.nextElementSibling.style.display=\'flex\';" ' +
            'onload="this.style.display=\'block\';this.nextElementSibling.style.display=\'none\';">' +
            fallbackHtml.replace('display:flex', 'display:none') +
            '</div>';
    } else {
        // 无封面URL，直接显示彩色占位符
        return '<div style="height:' + s.height + ';width:' + (s.width||'100%') +
            ';background:linear-gradient(135deg,' + colors[0] + ',' + colors[1] +
            ');display:flex;align-items:center;justify-content:center;border-radius:4px;">' +
            '<span style="font-size:' + s.text + ';color:#fff;font-weight:bold;">' +
            firstChar + '</span></div>';
    }
}

// 页面加载时：如果 localStorage 有 user，同时刷新一下 session
(function() {
    const user = JSON.parse(localStorage.getItem('user') || 'null');
    if (user) {
        fetch('/api/auth/me').then(r => r.json()).then(res => {
            if (res.code === 200) {
                localStorage.setItem('user', JSON.stringify(res.data));
            } else {
                localStorage.removeItem('user');
            }
        }).catch(() => {});
    }
})();
