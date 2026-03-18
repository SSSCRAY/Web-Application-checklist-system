const API = 'https://checklist-system.up.railway.app';

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

// Кастомный confirm вместо браузерного
function showConfirm(message) {
    return new Promise(resolve => {
        // Удаляем старый если есть
        const old = document.getElementById('_confirm_bg');
        if (old) old.remove();

        const bg = document.createElement('div');
        bg.id = '_confirm_bg';
        bg.style.cssText = 'position:fixed;inset:0;background:rgba(5,5,10,0.8);backdrop-filter:blur(6px);z-index:1000;display:flex;align-items:center;justify-content:center;padding:24px';

        bg.innerHTML = `
            <div style="background:#11111a;border:1px solid rgba(160,100,255,0.18);border-radius:2px;padding:32px 28px;width:100%;max-width:360px;position:relative">
                <div style="font-family:'Cormorant Garamond',serif;font-size:20px;font-weight:300;margin-bottom:20px;color:#e8e4f0">${message}</div>
                <div style="display:flex;gap:10px;justify-content:flex-end">
                    <button id="_confirm_no" style="padding:10px 20px;border:1px solid rgba(160,100,255,0.08);background:transparent;color:rgba(232,228,240,0.44);font-family:'DM Mono',monospace;font-size:10px;letter-spacing:.15em;text-transform:uppercase;cursor:pointer;border-radius:1px">Отмена</button>
                    <button id="_confirm_yes" style="padding:10px 20px;border:1px solid rgba(255,107,138,0.35);background:transparent;color:#ff6b8a;font-family:'DM Mono',monospace;font-size:10px;letter-spacing:.15em;text-transform:uppercase;cursor:pointer;border-radius:1px">Подтвердить</button>
                </div>
            </div>`;

        document.body.appendChild(bg);

        document.getElementById('_confirm_yes').onclick = () => { bg.remove(); resolve(true); };
        document.getElementById('_confirm_no').onclick  = () => { bg.remove(); resolve(false); };
        bg.addEventListener('click', e => { if (e.target === bg) { bg.remove(); resolve(false); } });
    });
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

