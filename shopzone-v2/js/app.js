const API = 'http://localhost:8080';
const APP = {
  token: () => localStorage.getItem('token'),
  userId: () => localStorage.getItem('userId'),
  userName: () => localStorage.getItem('userName') || 'User',
  userRole: () => localStorage.getItem('userRole'),
  cart: () => JSON.parse(localStorage.getItem('cart') || '[]'),
  wishlist: () => JSON.parse(localStorage.getItem('wishlist') || '[]'),
  saveCart: (c) => { localStorage.setItem('cart', JSON.stringify(c)); APP.updateCartBadge(); },
  saveWishlist: (w) => localStorage.setItem('wishlist', JSON.stringify(w)),

  headers: () => ({
    'Content-Type': 'application/json',
    'Authorization': 'Bearer ' + APP.token()
  }),

  async get(url) {
    const r = await fetch(API + url, { headers: APP.headers() });
    if (r.status === 401) { APP.logout(); return null; }
    return r.json();
  },

  async post(url, data, auth = true) {
    const h = { 'Content-Type': 'application/json' };
    if (auth) h['Authorization'] = 'Bearer ' + APP.token();
    const r = await fetch(API + url, { method: 'POST', headers: h, body: JSON.stringify(data) });
    return { ok: r.ok, status: r.status, data: await r.json() };
  },

  async put(url, data) {
    const r = await fetch(API + url, { method: 'PUT', headers: APP.headers(), body: JSON.stringify(data) });
    return { ok: r.ok, data: await r.json() };
  },

  async delete(url) {
    const r = await fetch(API + url, { method: 'DELETE', headers: APP.headers() });
    return r.ok;
  },

  toast(msg, type = 'info', duration = 3000) {
    let container = document.getElementById('toastContainer');
    if (!container) {
      container = document.createElement('div');
      container.id = 'toastContainer';
      container.className = 'toast-container';
      document.body.appendChild(container);
    }
    const icons = { success: '✅', error: '❌', info: 'ℹ️', warning: '⚠️' };
    const t = document.createElement('div');
    t.className = `toast ${type}`;
    t.innerHTML = `<span>${icons[type]}</span><span>${msg}</span>`;
    container.appendChild(t);
    setTimeout(() => t.style.opacity = '0', duration - 300);
    setTimeout(() => t.remove(), duration);
  },

  addToCart(id, name, price, brand = '') {
    let cart = APP.cart();
    const ex = cart.find(i => i.id === id);
    if (ex) { ex.quantity++; APP.toast(`${name} quantity updated`, 'info'); }
    else { cart.push({ id, name, price, brand, quantity: 1 }); APP.toast(`${name} added to cart! 🛒`, 'success'); }
    APP.saveCart(cart);
  },

  toggleWishlist(id, name) {
    let w = APP.wishlist();
    const idx = w.indexOf(id);
    if (idx === -1) { w.push(id); APP.toast(`${name} added to wishlist ❤️`, 'success'); }
    else { w.splice(idx, 1); APP.toast(`${name} removed from wishlist`, 'info'); }
    APP.saveWishlist(w);
    return idx === -1;
  },

  updateCartBadge() {
    const cart = APP.cart();
    const count = cart.reduce((s, i) => s + i.quantity, 0);
    document.querySelectorAll('.cart-count').forEach(el => el.textContent = count);
  },

  guard() {
    if (!APP.token()) { window.location.href = '/index.html'; return false; }
    return true;
  },

  adminGuard() {
    if (!APP.token()) { window.location.href = '/index.html'; return false; }
    if (APP.userRole() !== 'ADMIN') { window.location.href = '/home.html'; return false; }
    return true;
  },

  logout() {
    localStorage.clear();
    window.location.href = '/index.html';
  },

  getCategoryEmoji(cat) {
    const map = { Electronics:'📱', Fashion:'👗', Home:'🏠', Books:'📚', Sports:'⚽', Beauty:'💄', Food:'🍕', Toys:'🧸', Appliances:'🔌', Furniture:'🛋️' };
    return map[cat] || '🛍️';
  },

  formatPrice: (p) => '₹' + Number(p).toLocaleString('en-IN'),

  formatDate: (d) => new Date(d).toLocaleDateString('en-IN', { day:'numeric', month:'long', year:'numeric' }),

  renderNavbar(page = '') {
    const isAdmin = APP.userRole() === 'ADMIN';
    const name = APP.userName();
    const cartCount = APP.cart().reduce((s, i) => s + i.quantity, 0);
    return `
    <nav class="navbar">
      <div class="nav-inner">
        <div class="nav-logo" onclick="location.href='/home.html'">Shop<span>Zone</span></div>
        <div class="search-wrap">
          <select id="searchCat">
            <option value="">All</option>
            <option>Electronics</option><option>Fashion</option><option>Home</option>
            <option>Books</option><option>Sports</option><option>Beauty</option>
          </select>
          <input type="text" id="searchInput" placeholder="Search products, brands..."/>
          <button onclick="APP.doSearch()">🔍</button>
        </div>
        <div class="nav-actions">
          <button class="theme-toggle" onclick="APP.toggleTheme()" title="Toggle dark mode">🌙</button>
          <div class="nav-btn-wrap">
            <button class="nav-btn" onclick="location.href='/pages/profile.html'">
              <i>👤</i><small>${name.slice(0,8)}</small>
            </button>
          </div>
          ${isAdmin ? `<button class="nav-btn" onclick="location.href='/pages/admin.html'"><i>⚙️</i><small>Admin</small></button>` : ''}
          <div class="nav-btn-wrap">
            <button class="nav-btn" onclick="location.href='/pages/cart.html'">
              <i>🛒</i><small>Cart</small>
              <span class="cart-count">${cartCount}</span>
            </button>
          </div>
          <button class="nav-btn" onclick="APP.logout()"><i>🚪</i><small>Logout</small></button>
        </div>
      </div>
    </nav>`;
  },

  renderFooter() {
    return `
    <footer>
      <div class="container">
        <div class="footer-grid">
          <div class="footer-col">
            <h4>About</h4>
            <a>About Us</a><a>Careers</a><a>Press</a><a>Blog</a>
          </div>
          <div class="footer-col">
            <h4>Help</h4>
            <a>Payments</a><a>Shipping</a><a>Returns</a><a>FAQ</a>
          </div>
          <div class="footer-col">
            <h4>Policy</h4>
            <a>Return Policy</a><a>Terms of Use</a><a>Privacy</a>
          </div>
          <div class="footer-col">
            <h4>Social</h4>
            <a>Facebook</a><a>Twitter</a><a>Instagram</a><a>YouTube</a>
          </div>
          <div class="footer-col">
            <h4>Mail Us</h4>
            <a>ShopZone Internet Pvt Ltd</a>
            <a>support@shopzone.com</a>
            <a>1800-XXX-XXXX</a>
          </div>
        </div>
        <div class="footer-bottom">
          <p>© 2024 ShopZone.com — India's Own Marketplace</p>
          <div class="footer-payments">💳 🏦 📱 💵</div>
        </div>
      </div>
    </footer>`;
  },

  doSearch() {
    const q = document.getElementById('searchInput')?.value?.trim();
    const cat = document.getElementById('searchCat')?.value;
    if (q || cat) window.location.href = `/pages/products.html?search=${encodeURIComponent(q)}&category=${encodeURIComponent(cat)}`;
  },

  toggleTheme() {
    const isDark = document.documentElement.getAttribute('data-theme') === 'dark';
    document.documentElement.setAttribute('data-theme', isDark ? 'light' : 'dark');
    localStorage.setItem('theme', isDark ? 'light' : 'dark');
  },

  initTheme() {
    const t = localStorage.getItem('theme') || 'light';
    document.documentElement.setAttribute('data-theme', t);
  }
};

document.addEventListener('DOMContentLoaded', () => { APP.initTheme(); APP.updateCartBadge(); });
