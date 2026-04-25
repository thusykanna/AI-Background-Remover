import { useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '@clerk/clerk-react';
import { AppContext } from '../context/AppContext';

const TryNow = () => {
    const { removeBg } = useContext(AppContext);
    const { isSignedIn, getToken, openSignIn } = useAuth();
    const navigate = useNavigate();

    const handleFileChange = async (e) => {
        const file = e.target.files?.[0];
        if (!file) return;
        if (!isSignedIn) {
            openSignIn({});
            return;
        }
        navigate('/result');
        await removeBg(file, getToken);
        e.target.value = '';
    };

    return (
      <div className="flex flex-col items-center justify-center bg-white px-4">
        <h2 className="text-3xl md:text-4xl font-bold text-gray-900 mb-7 text-center">
          Remove Image Background.
        </h2>
        <p className="text-gray-500 mb-8 text-center">
          Get a transparent background for any image.
        </p>
        <div className="bg-white rounded-2xl shadow-lg p-10 flex flex-col items-center space-y-4">
          <input type="file" id="upload2" hidden accept="image/*" onChange={handleFileChange} />
          <label
            htmlFor="upload2"
            className="bg-indigo-600 hover:bg-blue-700 text-white font-semibold py-3 px-6 rounded-full text-lg cursor-pointer"
          >
            Upload Image
          </label>
          <p className="text-gray-500 text-sm">
            or drop a file or paste image
          </p>
        </div>
      </div>
    );
};

export default TryNow;