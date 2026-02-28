const API = 'https://web-application-checklist-system-production.up.railway.app';

function getToken() { return localStorage.getItem('token'); }
function getRole()  { return localStorage.getItem('role'); }
function getName()  { return localStorage.getItem('fullName'); }

function authHeaders() {
    return { 'Content-Type': 'application/json', 'Authorization': `Bearer ${getToken()}` };
}

function guardAuth(requiredRole) {
    if (!getToken()) { window.location.href = 'login.html'; return false; }
    if (requiredRole && getRole() !== requiredRole) { window.location.href = 'checklists.html'; return false; }
    return true;
}

function logout() { localStorage.clear(); window.location.href = 'login.html'; }

function showToast(msg, type = 'ok') {
    const el = document.getElementById('toast');
    if (!el) return;
    el.textContent = msg;
    el.className = `toast ${type} show`;
    setTimeout(() => el.classList.remove('show'), 3000);
}

function escHtml(s) {
    return String(s).replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;');
}

function initParticles(opacity = 0.5, count = 100) {
    const canvas = document.getElementById('particles');
    if (!canvas) return;
    const ctx = canvas.getContext('2d');
    let W, H, pts = [];

    function resize() { W = canvas.width = innerWidth; H = canvas.height = innerHeight; }
    function mkP() {
        return { x:Math.random()*W, y:Math.random()*H, r:Math.random()*1.6+0.2,
                 vx:(Math.random()-.5)*.28, vy:(Math.random()-.5)*.28,
                 a:Math.random()*.55+.1, p:Math.random()*Math.PI*2 };
    }
    function init() { pts = Array.from({length:count}, mkP); }
    function draw() {
        ctx.clearRect(0,0,W,H);
        pts.forEach(p => {
            p.p += .007; const a = p.a*(.5+.5*Math.sin(p.p));
            ctx.beginPath(); ctx.arc(p.x,p.y,p.r,0,Math.PI*2);
            ctx.fillStyle = `rgba(157,95,255,${a})`; ctx.fill();
            p.x+=p.vx; p.y+=p.vy;
            if(p.x<-5)p.x=W+5; if(p.x>W+5)p.x=-5;
            if(p.y<-5)p.y=H+5; if(p.y>H+5)p.y=-5;
        });
        for(let i=0;i<pts.length;i++) for(let j=i+1;j<pts.length;j++) {
            const dx=pts[i].x-pts[j].x, dy=pts[i].y-pts[j].y;
            const d=Math.sqrt(dx*dx+dy*dy);
            if(d<85){ ctx.beginPath(); ctx.moveTo(pts[i].x,pts[i].y); ctx.lineTo(pts[j].x,pts[j].y);
                ctx.strokeStyle=`rgba(157,95,255,${.07*(1-d/85)})`; ctx.lineWidth=.5; ctx.stroke(); }
        }
        requestAnimationFrame(draw);
    }
    canvas.style.opacity = opacity;
    resize(); init(); draw();
    window.addEventListener('resize',()=>{resize();init();});
}

// API helpers
const api = {
    async get(path) {
        const r = await fetch(API+path, { headers: authHeaders() });
        if (r.status === 401) { logout(); return null; }
        return r.json();
    },
    async post(path, body) {
        return fetch(API+path, { method:'POST', headers:authHeaders(), body:JSON.stringify(body) });
    },
    async put(path, body) {
        return fetch(API+path, { method:'PUT', headers:authHeaders(), body:JSON.stringify(body) });
    },
    async patch(path) {
        return fetch(API+path, { method:'PATCH', headers:authHeaders() });
    },
    async delete(path) {
        return fetch(API+path, { method:'DELETE', headers:authHeaders() });
    }
};
