/* ============================================================
   智慧书阁 - 学生端公共JS
   版本: 2.0.0
   用法: 在每页调用 initStudentPage('页面标题')
   ============================================================ */

// ---------- 鉴权检查 ----------
async function initStudentPage(title) {
  const user = auth.getUser();
  if (!user) {
    window.location.href = '/student/login.html';
    return false;
  }
  const valid = await auth.verify();
  if (!valid) {
    window.location.href = '/student/login.html';
    return false;
  }

  // 如果页面已有 Hero 区（新沉浸式布局），跳过导航栏注入和 .student-main 包装
  if (document.querySelector('.student-hero')) {
    return true;
  }

  // 旧版布局兼容
  document.body.insertAdjacentHTML('afterbegin', getNavbarHTML(user, title));
  var main = document.querySelector('.student-main');
  if (!main) {
    var existing = document.body.children;
    var wrapper = document.createElement('div');
    wrapper.className = 'student-main';
    while (existing.length > 1) {
      var child = existing[1];
      if (child.tagName === 'SCRIPT') break;
      wrapper.appendChild(child);
    }
    document.body.appendChild(wrapper);
  }
  return true;
}

// 未登录时的导航（用于公开页面如首页/详情）
function renderStudentNav() {
  const user = auth.getUser();
  const navEl = document.getElementById('studentNavRight');
  if (!navEl) return;
  if (user) {
    const name = user.realName || user.username;
    navEl.innerHTML = `
      <a href="/student/" class="btn btn-outline-light btn-sm me-2">我的首页</a>
      <a href="/student/books" class="btn btn-outline-light btn-sm me-2">浏览图书</a>
      <a href="/student/my-borrows" class="btn btn-outline-light btn-sm me-2">我的借阅</a>
      <a href="/student/profile" class="btn btn-outline-light btn-sm me-2">个人中心</a>
      <button class="btn btn-outline-light btn-sm" onclick="studentLogout()">退出</button>`;
  } else {
    navEl.innerHTML = `
      <a href="/student/login.html" class="btn btn-outline-light btn-sm me-2">登录</a>
      <a href="/student/register.html" class="btn btn-outline-light btn-sm">注册</a>`;
  }
}

// ---------- 导航栏HTML ----------
function getNavbarHTML(user, title) {
  const name = user.realName || user.username;
  const initial = name.charAt(0);
  return `
<nav class="navbar navbar-expand-lg student-navbar">
  <div class="container">
    <a class="navbar-brand" href="/student/">
      <i class="bi bi-book-half"></i> 智慧书阁
    </a>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#studentNav">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="studentNav">
      <ul class="navbar-nav me-auto">
        <li class="nav-item"><a class="nav-link" href="/student/"><i class="bi bi-house"></i> 首页</a></li>
        <li class="nav-item"><a class="nav-link" href="/student/books"><i class="bi bi-search"></i> 图书检索</a></li>
        <li class="nav-item"><a class="nav-link" href="/student/my-borrows"><i class="bi bi-journal-text"></i> 我的借阅</a></li>
      </ul>
      <div class="d-flex align-items-center gap-2">
        <div class="dropdown">
          <button class="btn btn-outline-light btn-sm dropdown-toggle" data-bs-toggle="dropdown">
            <span class="avatar-sm me-1">${initial}</span> ${name}
          </button>
          <ul class="dropdown-menu dropdown-menu-end">
            <li><a class="dropdown-item" href="/student/profile"><i class="bi bi-person"></i> 个人中心</a></li>
            <li><hr class="dropdown-divider"></li>
            <li><a class="dropdown-item" href="#" onclick="studentLogout()"><i class="bi bi-box-arrow-right"></i> 退出登录</a></li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</nav>`;
}

// ---------- 退出登录 ----------
async function studentLogout() {
  try { await api.post('/auth/logout'); } catch (e) { /* ignore */ }
  auth.clearUser();
  window.location.href = '/student/login.html';
}

// ---------- 借阅图书 ----------
async function borrowBook(bookId) {
  const res = await api.post('/borrow/' + bookId);
  if (res.code === 200) {
    showToast('success', '借阅成功！');
    setTimeout(function() { location.reload(); }, 1500);
  } else {
    showToast('error', res.message || '借阅失败');
  }
}

// ---------- 归还图书 ----------
async function returnBorrow(recordId) {
  const res = await api.post('/borrow/return/' + recordId);
  if (res.code === 200) {
    showToast('success', '归还成功！');
    setTimeout(function() { location.reload(); }, 1500);
  } else {
    showToast('error', res.message || '归还失败');
  }
}
