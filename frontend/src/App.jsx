import React, { useEffect, useState } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { setToken } from './main.jsx'

const API = import.meta.env.VITE_API_URL || 'http://localhost:8080'
async function request(path, token, options = {}) {
  const response = await fetch(`${API}${path}`, {
    ...options,
    headers: { 'Content-Type': 'application/json', ...(token ? { Authorization: `Bearer ${token}` } : {}), ...(options.headers || {}) }
  })
  const body = response.status === 204 ? null : await response.json().catch(() => null)
  if (!response.ok) throw new Error(body?.error || `Request failed (${response.status})`)
  return body
}

export default function App() {
  const token = useSelector(s => s.session.token)
  const dispatch = useDispatch()
  const [mode, setMode] = useState('login')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [content, setContent] = useState('')
  const [scheduledAt, setScheduledAt] = useState('')
  const [posts, setPosts] = useState([])
  const [analytics, setAnalytics] = useState(null)
  const [error, setError] = useState('')
  const [busy, setBusy] = useState(false)

  async function auth(e) {
    e.preventDefault(); setError(''); setBusy(true)
    try {
      const result = await request(`/api/auth/${mode}`, '', { method: 'POST', body: JSON.stringify({ email, password }) })
      dispatch(setToken(result.token))
    } catch (err) { setError(err.message) } finally { setBusy(false) }
  }
  async function refresh() {
    if (!token) return
    try {
      const [p, a] = await Promise.all([request('/api/posts', token), request('/api/analytics/summary', token)])
      setPosts(p); setAnalytics(a); setError('')
    } catch (err) { setError(err.message) }
  }
  useEffect(() => { refresh() }, [token])
  async function createPost(e) {
    e.preventDefault(); setError('')
    try {
      await request('/api/posts', token, { method: 'POST', body: JSON.stringify({ content, scheduledAt: scheduledAt ? new Date(scheduledAt).toISOString() : null }) })
      setContent(''); setScheduledAt(''); await refresh()
    } catch (err) { setError(err.message) }
  }
  async function deletePost(id) {
    try { await request(`/api/posts/${id}`, token, { method: 'DELETE' }); await refresh() }
    catch (err) { setError(err.message) }
  }

  return <main className="shell">
    <header><div><p className="eyebrow">FULL STACK · EXPERIMENTS 3.3.1 + CAPSTONE</p><h1>Social Media Manager</h1><p className="sub">Compose, schedule and review your content in one place.</p></div>
      {token && <button className="secondary" onClick={() => { dispatch(setToken('')); setPosts([]); setAnalytics(null) }}>Log out</button>}
    </header>
    {error && <div className="error" role="alert">{error}</div>}
    {!token ? <section className="panel auth"><h2>{mode === 'login' ? 'Welcome back' : 'Create your account'}</h2><p>Use an email and a password of at least 8 characters.</p>
      <form onSubmit={auth}><label>Email<input type="email" required value={email} onChange={e=>setEmail(e.target.value)} /></label><label>Password<input type="password" minLength="8" required value={password} onChange={e=>setPassword(e.target.value)} /></label><button disabled={busy}>{busy ? 'Please wait…' : mode === 'login' ? 'Log in' : 'Register'}</button></form>
      <button className="link" onClick={()=>setMode(mode === 'login' ? 'register' : 'login')}>{mode === 'login' ? 'New here? Register' : 'Already registered? Log in'}</button>
    </section> : <>
      <section className="stats">
        <article className="panel"><span>Total posts</span><strong>{analytics?.totalPosts ?? '—'}</strong></article>
        <article className="panel"><span>Drafts</span><strong>{analytics?.byStatus?.DRAFT ?? 0}</strong></article>
        <article className="panel"><span>Scheduled</span><strong>{analytics?.byStatus?.SCHEDULED ?? 0}</strong></article>
      </section>
      <section className="panel"><h2>Post composer</h2><form onSubmit={createPost}><label>Post content<textarea required maxLength="5000" rows="4" value={content} onChange={e=>setContent(e.target.value)} placeholder="What would you like to share?" /></label><label>Schedule (optional)<input type="datetime-local" value={scheduledAt} onChange={e=>setScheduledAt(e.target.value)} /></label><button>Create {scheduledAt ? 'scheduled post' : 'draft'}</button></form></section>
      <section className="panel"><div className="section-head"><h2>Your posts</h2><button className="secondary" onClick={refresh}>Refresh</button></div>
        {posts.length === 0 ? <p className="muted">No posts yet. Create your first post above.</p> : <div className="post-list">{posts.map(p=><article className="post" key={p.id}><div><span className={`pill ${p.status.toLowerCase()}`}>{p.status}</span><p>{p.content}</p><small>{p.scheduledAt ? `Scheduled: ${new Date(p.scheduledAt).toLocaleString()}` : 'Not scheduled'}</small></div><button className="danger" onClick={()=>deletePost(p.id)}>Delete</button></article>)}</div>}
      </section>
    </>}
    <footer>Spring Boot REST API · JWT · PostgreSQL · Kafka · Docker · GitHub Actions</footer>
  </main>
}
