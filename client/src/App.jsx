import Home from "./pages/Home";
import Result from "./pages/Result";
import Menubar from "./components/Menubar";
import Footer from "./components/Footer";
import { Routes, Route } from "react-router-dom";
import { Toaster } from "react-hot-toast";
import UserSyncHandler from "./components/UserSyncHandler";


const App = () => {
  return (
    <div>
      <UserSyncHandler />
      <Menubar/>
      <Toaster/>
      <Routes>
        <Route path="/" element={<Home/>}/>
        <Route path="/result" element={<Result/>}/>
      </Routes>
      <Footer/>
    </div>
  )
}

export default App;