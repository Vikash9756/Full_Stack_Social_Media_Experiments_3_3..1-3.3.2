import React from 'react'
import { createRoot } from 'react-dom/client'
import { Provider } from 'react-redux'
import { configureStore, createSlice } from '@reduxjs/toolkit'
import App from './App.jsx'
import './style.css'

const sessionSlice = createSlice({
  name: 'session',
  initialState: { token: sessionStorage.getItem('token') || '' },
  reducers: {
    setToken: (state, action) => { state.token = action.payload; if (action.payload) sessionStorage.setItem('token', action.payload); else sessionStorage.removeItem('token') }
  }
})
export const { setToken } = sessionSlice.actions
export const store = configureStore({ reducer: { session: sessionSlice.reducer } })
createRoot(document.getElementById('root')).render(<Provider store={store}><App /></Provider>)
