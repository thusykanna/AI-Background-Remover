import Home from "./pages/Home";
import Menubar from "./components/menubar";
import Footer from "./components/Footer";
import { Routes, Route } from "react-router-dom";
import { Toaster } from "react-hot-toast";

const App = () => {
  return (
    <div>
      <Menubar/>
      <Toaster/>
      <Routes>
        <Route path="/" element={<Home/>}/>
      </Routes>
      <Footer/>
    </div>
  )
}

export default App;