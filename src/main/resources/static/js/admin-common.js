/* ============================================================
   智慧书阁 - 管理员端公共JS
   版本: 2.1.0 | 侧边栏 Linear/Notion 风格
   ============================================================ */

// ---------- 鉴权检查 ----------
async function initAdminPage(title) {
  const user = auth.getUser();
  if (!user || user.role !== 1) {
    window.location.href = '/admin/login.html';
    return false;
  }

  const valid = await auth.verify();
  if (!valid) {
    window.location.href = '/admin/login.html';
    return false;
  }

  // 注入布局
  document.body.insertAdjacentHTML('afterbegin', getSidebarHTML(user));
  document.body.insertAdjacentHTML('afterbegin', getTopbarHTML(title));

  highlightCurrentNav();
  bindSidebarToggle();
  startTopbarClock();

  return true;
}

// ---------- 顶部时钟 ----------
function startTopbarClock() {
  function updateClock() {
    var el = document.getElementById('topbarClock');
    if (!el) return;
    var now = new Date();
    var pad = function(n) { return String(n).padStart(2, '0'); };
    el.textContent = now.getFullYear() + '-' + pad(now.getMonth()+1) + '-' + pad(now.getDate())
      + ' ' + pad(now.getHours()) + ':' + pad(now.getMinutes()) + ':' + pad(now.getSeconds());
  }
  updateClock();
  setInterval(updateClock, 1000);
}

// ---------- 全屏切换 ----------
function toggleFullscreen() {
  if (document.fullscreenElement) {
    document.exitFullscreen();
  } else {
    document.documentElement.requestFullscreen();
  }
}

// ---------- 侧边栏HTML ----------
function getSidebarHTML(user) {
  var name = user.realName || user.username || '管理员';
  var initial = name.charAt(0);
  return [
  '<aside class="admin-sidebar" id="adminSidebar">',
    '<div class="sidebar-user-card">',
      '<div class="user-avatar">' + initial + '</div>',
      '<div class="user-name">' + name + '</div>',
      '<div class="user-role">管理员</div>',
    '</div>',
    '<nav class="sidebar-nav">',
      '<a href="/admin/" class="nav-item" data-page="dashboard"><i class="bi bi-speedometer2"></i> <span>仪表盘</span></a>',
      '<a href="/admin/books" class="nav-item" data-page="books"><i class="bi bi-book"></i> <span>图书管理</span></a>',
      '<a href="/admin/users" class="nav-item" data-page="users"><i class="bi bi-people"></i> <span>读者管理</span></a>',
      '<a href="/admin/borrows" class="nav-item" data-page="borrows"><i class="bi bi-journal-check"></i> <span>借阅管理</span></a>',
      '<a href="/admin/overdue" class="nav-item" data-page="overdue"><i class="bi bi-exclamation-triangle"></i> <span>逾期管理</span></a>',
      '<a href="/admin/categories" class="nav-item" data-page="categories"><i class="bi bi-tags"></i> <span>分类管理</span></a>',
      '<a href="/admin/permissions" class="nav-item" data-page="permissions"><i class="bi bi-shield-check"></i> <span>权限管理</span></a>',
      '<a href="/admin/settings" class="nav-item" data-page="settings"><i class="bi bi-gear"></i> <span>系统设置</span></a>',
    '</nav>',
    '<div class="sidebar-footer">',
      '<div class="footer-row">',
        '<span class="sidebar-version">v3.0.0</span>',
        '<div>',
          '<button class="theme-toggle" onclick="toggleDarkMode()" title="切换主题"><i class="bi bi-moon" id="themeIcon"></i></button>',
          '<button class="theme-toggle" onclick="adminLogout()" title="退出登录"><i class="bi bi-box-arrow-right"></i></button>',
        '</div>',
      '</div>',
    '</div>',
  '</aside>',
  '<div class="sidebar-overlay" id="sidebarOverlay" onclick="closeSidebar()"></div>'
  ].join('\n');
}

// ---------- 顶栏HTML ----------
function getTopbarHTML(title) {
  return [
  '<header class="admin-topbar" id="adminTopbar">',
    '<button class="hamburger" onclick="toggleSidebar()"><i class="bi bi-list"></i></button>',
    '<div class="breadcrumb-nav"><span>' + (title || '管理后台') + '</span></div>',
    '<div class="topbar-actions">',
      '<button class="topbar-btn" onclick="toggleFullscreen()" title="全屏模式"><i class="bi bi-arrows-fullscreen"></i></button>',
      '<button class="topbar-btn" title="消息通知"><i class="bi bi-bell"></i><span class="badge-dot"></span></button>',
      '<span class="topbar-clock" id="topbarClock"></span>',
      '<a href="/student/" target="_blank" class="btn btn-sm btn-outline-primary"><i class="bi bi-eye"></i> 前台</a>',
    '</div>',
  '</header>'
  ].join('\n');
}

// ---------- 暗黑模式 ----------
function toggleDarkMode() {
  var icon = document.getElementById('themeIcon');
  if (!icon) return;
  var isDark = document.body.classList.toggle('dark-mode');
  icon.className = isDark ? 'bi bi-sun' : 'bi bi-moon';
}

// ---------- 当前页高亮 ----------
function highlightCurrentNav() {
  var path = window.location.pathname;
  document.querySelectorAll('.sidebar-nav .nav-item').forEach(function(a) {
    var href = a.getAttribute('href');
    if (path === href || (href !== '/admin/' && path.startsWith(href))) {
      a.classList.add('active');
    }
  });
}

// ---------- 侧边栏切换 ----------
function bindSidebarToggle() {
  document.addEventListener('click', function(e) {
    var sidebar = document.getElementById('adminSidebar');
    var hamburger = document.querySelector('.hamburger');
    if (!sidebar || !sidebar.classList.contains('open')) return;
    if (!sidebar.contains(e.target) && e.target !== hamburger && !hamburger.contains(e.target)) {
      closeSidebar();
    }
  });
}

function toggleSidebar() {
  var sidebar = document.getElementById('adminSidebar');
  var overlay = document.getElementById('sidebarOverlay');
  if (!sidebar) return;
  var opening = !sidebar.classList.contains('open');
  sidebar.classList.toggle('open');
  if (overlay) overlay.classList.toggle('open');
  document.body.style.overflow = opening && window.innerWidth <= 991 ? 'hidden' : '';
}

function closeSidebar() {
  var sidebar = document.getElementById('adminSidebar');
  var overlay = document.getElementById('sidebarOverlay');
  if (sidebar) sidebar.classList.remove('open');
  if (overlay) overlay.classList.remove('open');
  document.body.style.overflow = '';
}

// ---------- 退出登录 ----------
async function adminLogout() {
  try { await api.post('/auth/logout'); } catch (e) { /* ignore */ }
  auth.clearUser();
  window.location.href = '/admin/login.html';
}
