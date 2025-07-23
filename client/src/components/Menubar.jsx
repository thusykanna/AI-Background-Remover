import { useState } from "react";
import { assets } from "../assets/assets";

const Menubar = () => {

    const [menuOpen, setMenuOpen] = useState(false);

    return (
        <nav className="bg-white px-8 py-4 flex justify-between items-center">
            {/* left side logo: logo + text */}
            <div className="flex item-center space-x-2">
                <img src={assets.logo} alt="logo" className="h-8 w-8 object-contain cursor-pointer" />
                <span className="text-2xl font-semibold text-indigo-700 cursor-pointer">
                    remove.<span className="text-gray-400 cursor-pointer">bg</span>
                </span>
            </div>

            {/* right side: action button */}
            <div className="hidden md:flex items-center space-x-4">
                <button className="text-gray-700 hover:text-blue-500 px-4 py-2 font-medium">
                    Login
                </button>
                <button className="bg-gray-100 hover:bg-gray-200 text-gray-700 font-medium px-4 py-2 rounded-full transition-all">
                    Sign up
                </button>
            </div>

        </nav>
    )
}

export default Menubar;