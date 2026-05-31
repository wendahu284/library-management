/* ============================================================
   智慧书阁 - 全局共享JS工具
   版本: 2.0.0
   ============================================================ */

// ---------- API 请求封装 ----------
const API_BASE = '/api';

const api = {
  async request(url, options = {}) {
    const config = {
      headers: { 'Content-Type': 'application/json', ...options.headers },
      ...options
    };
    try {
      const response = await fetch(API_BASE + url, config);
      const data = await response.json();
      return data;
    } catch (err) {
      console.error('API Error:', err);
      return { code: 500, message: '网络异常，请稍后重试' };
    }
  },
  get(url, params) {
    let qs = '';
    if (params) {
      const sp = new URLSearchParams();
      Object.entries(params).forEach(([k, v]) => {
        if (v !== undefined && v !== null && v !== '') sp.append(k, v);
      });
      qs = '?' + sp.toString();
    }
    return this.request(url + qs);
  },
  post(url, data)      { return this.request(url, { method: 'POST', body: JSON.stringify(data) }); },
  put(url, data)       { return this.request(url, { method: 'PUT',  body: JSON.stringify(data) }); },
  del(url)             { return this.request(url, { method: 'DELETE' }); }
};

// ---------- 消息提示 ----------
function showAlert(containerId, type, message) {
  const el = document.getElementById(containerId);
  if (!el) return;
  const icons = { success: 'check-circle', danger: 'exclamation-circle', warning: 'exclamation-triangle', info: 'info-circle' };
  const icon = icons[type] || 'info-circle';
  el.innerHTML = `<div class="alert alert-${type} d-flex align-items-center alert-dismissible fade show" role="alert">
    <i class="bi bi-${icon} me-2"></i> ${message}
    <button type="button" class="btn-close" data-bs-dismiss="alert"></button></div>`;
}

function showToast(type, message) {
  let container = document.querySelector('.toast-container');
  if (!container) {
    container = document.createElement('div');
    container.className = 'toast-container';
    document.body.appendChild(container);
  }
  const icons = { success: 'bi-check-circle-fill text-success', error: 'bi-x-circle-fill text-danger', warning: 'bi-exclamation-triangle-fill text-warning', info: 'bi-info-circle-fill text-info' };
  const item = document.createElement('div');
  item.className = 'toast-item ' + type;
  item.innerHTML = '<i class="bi ' + (icons[type] || icons.info) + '"></i> ' + message + '<div class="toast-progress"></div>';
  container.appendChild(item);
  setTimeout(function() {
    item.style.opacity = '0';
    item.style.transition = 'opacity .3s';
    setTimeout(function() { item.remove(); }, 300);
  }, 3000);
}

// ---------- 数字递增动画 ----------
function countUp(el, target, duration) {
  if (!el) return;
  var start = 0;
  var step = Math.max(1, Math.floor(target / (duration / 16)));
  var current = start;
  function tick() {
    current += step;
    if (current >= target) {
      el.textContent = target;
      el.classList.remove('counting');
      return;
    }
    el.textContent = current;
    el.classList.add('counting');
    requestAnimationFrame(tick);
  }
  tick();
}

// ---------- 分页渲染 ----------
function renderPagination(containerId, pageData, onPageChange) {
  const el = document.getElementById(containerId);
  if (!el) return;
  if (!pageData || pageData.totalPages <= 1) { el.innerHTML = ''; return; }
  const { pageNum, totalPages, total } = pageData;
  let html = '<div class="pagination-bar">';
  html += `<span class="pagination-info">共 <b>${total}</b> 条记录，第 <b>${pageNum}</b>/${totalPages} 页</span>`;
  html += '<div class="pagination-controls">';
  html += `<button class="page-btn" onclick="${onPageChange}(1)" ${pageNum <= 1 ? 'disabled' : ''}>«</button>`;
  html += `<button class="page-btn" onclick="${onPageChange}(${pageNum - 1})" ${pageNum <= 1 ? 'disabled' : ''}>‹</button>`;
  html += `<span class="page-current">${pageNum}</span>`;
  html += `<button class="page-btn" onclick="${onPageChange}(${pageNum + 1})" ${pageNum >= totalPages ? 'disabled' : ''}>›</button>`;
  html += `<button class="page-btn" onclick="${onPageChange}(${totalPages})" ${pageNum >= totalPages ? 'disabled' : ''}>»</button>`;
  html += '</div></div>';
  el.innerHTML = html;
}

// ---------- 日期格式化 ----------
function formatDate(dateStr, full) {
  if (!dateStr) return '-';
  const s = dateStr.replace('T', ' ').substring(0, full ? 19 : 16);
  return s;
}

function dateToInput(dateStr) {
  if (!dateStr) return '';
  return dateStr.replace('T', ' ').substring(0, 10);
}

// ---------- 图书封面 ----------
const COVER_COLORS = [
  ['#667eea', '#764ba2'], ['#f093fb', '#f5576c'], ['#4facfe', '#00f2fe'],
  ['#43e97b', '#38f9d7'], ['#fa709a', '#fee140'], ['#a18cd1', '#fbc2eb'],
  ['#fccb90', '#d57eeb'], ['#e0c3fc', '#8ec5fc'], ['#f5576c', '#ff6f00'],
  ['#667eea', '#4facfe']
];

function hashColor(str) {
  let hash = 0;
  for (let i = 0; i < str.length; i++) {
    hash = ((hash << 5) - hash) + str.charCodeAt(i);
    hash |= 0;
  }
  return Math.abs(hash) % COVER_COLORS.length;
}

function getCoverUrl(book, size) {
  size = size || 'M';
  if (book.coverUrl && book.coverUrl.trim()) return book.coverUrl.trim();
  if (book.isbn) return 'https://covers.openlibrary.org/b/isbn/' + book.isbn + '-' + size + '.jpg';
  return '';
}

function getCoverHtml(book, size) {
  var coverUrl = getCoverUrl(book, size === 'large' ? 'L' : 'M');
  var title = book.title || '?';
  var firstChar = title.charAt(0);
  var colors = COVER_COLORS[hashColor(title)];
  var sizes = {
    small:  { height: '220px', icon: '3rem', text: '1.5rem' },
    medium: { height: '60px',  icon: '1.2rem', text: '0.9rem', width: '45px' },
    large:  { height: '400px', icon: '6rem', text: '3rem' }
  };
  var s = sizes[size] || sizes.small;
  var fallbackHtml = '<div style="height:' + s.height + ';width:' + (s.width || '100%') +
    ';background:linear-gradient(135deg,' + colors[0] + ',' + colors[1] +
    ');display:flex;align-items:center;justify-content:center;border-radius:4px;">' +
    '<span style="font-size:' + s.text + ';color:#fff;font-weight:bold;">' + firstChar + '</span></div>';

  if (coverUrl) {
    return '<div style="height:' + s.height + ';width:' + (s.width || '100%') +
      ';overflow:hidden;border-radius:4px;background:#f0f0f0;">' +
      '<img src="' + coverUrl + '" alt="' + title + '" style="width:100%;height:100%;object-fit:cover;" ' +
      'onerror="this.style.display=\'none\';this.nextElementSibling.style.display=\'flex\';" ' +
      'onload="this.style.display=\'block\';this.nextElementSibling.style.display=\'none\';">' +
      fallbackHtml.replace('display:flex', 'display:none') + '</div>';
  }
  return fallbackHtml;
}

// ---------- 认证管理 ----------
const AUTH_KEY = 'library_user';
const auth = {
  getUser() {
    try { return JSON.parse(localStorage.getItem(AUTH_KEY) || 'null'); } catch (e) { return null; }
  },
  setUser(user) {
    localStorage.setItem(AUTH_KEY, JSON.stringify(user));
  },
  clearUser() {
    localStorage.removeItem(AUTH_KEY);
  },
  isLoggedIn() {
    return !!this.getUser();
  },
  isAdmin() {
    const u = this.getUser();
    return u && u.role === 1;
  },
  async verify() {
    const user = this.getUser();
    if (!user) return false;
    try {
      const res = await api.get('/auth/me');
      if (res.code === 200) {
        this.setUser(res.data);
        return true;
      }
    } catch (e) { /* ignore */ }
    this.clearUser();
    return false;
  }
};

// ---------- 导出CSV ----------
function downloadCSV(filename, headers, rows) {
  const BOM = '﻿';
  let csv = BOM + headers.join(',') + '\n';
  rows.forEach(row => {
    csv += row.map(cell => {
      const v = (cell === null || cell === undefined) ? '' : String(cell);
      return v.includes(',') || v.includes('"') || v.includes('\n') ? '"' + v.replace(/"/g, '""') + '"' : v;
    }).join(',') + '\n';
  });
  const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
  const url = URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url; a.download = filename; a.click();
  URL.revokeObjectURL(url);
}

// ---------- localStorage 键值存储 ----------
const storage = {
  get(key, defaultVal) {
    try { const v = localStorage.getItem(key); return v ? JSON.parse(v) : defaultVal; } catch (e) { return defaultVal; }
  },
  set(key, val) {
    localStorage.setItem(key, JSON.stringify(val));
  }
};
