import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'
import { BrowserRouter } from 'react-router-dom'
import { ClerkProvider } from '@clerk/clerk-react'

const PUBLISHABLE_KEY = import.meta.env.VITE_CLERK_PUBLISHABLE_KEY;

if (!PUBLISHABLE_KEY) {
  throw new Error('VITE_CLERK_PUBLISHABLE_KEY is not defined. Please set it in your .env file.');
}

createRoot(document.getElementById('root')).render(
  <BrowserRouter>
    <ClerkProvider publishable_key = {PUBLISHABLE_KEY}>
      <App/>
    </ClerkProvider>
  </BrowserRouter>,
)
