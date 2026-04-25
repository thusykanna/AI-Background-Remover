import { useContext, useState, useCallback } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "@clerk/clerk-react";
import { AppContext } from "../context/AppContext";

const Result = () => {
  const { originalImage, resultImage, isProcessing, credits, removeBg } = useContext(AppContext);
  const { getToken } = useAuth();
  const navigate = useNavigate();
  const [sliderPosition, setSliderPosition] = useState(50);

  const handleDownload = () => {
    if (!resultImage) return;
    const a = document.createElement("a");
    a.href = resultImage;
    a.download = "removed-bg.png";
    a.click();
  };

  const handleNewImage = useCallback(async (e) => {
    const file = e.target.files?.[0];
    if (!file) return;
    await removeBg(file, getToken);
    e.target.value = "";
  }, [removeBg, getToken]);

  if (!originalImage) {
    return (
      <div className="min-h-[60vh] flex flex-col items-center justify-center gap-4">
        <p className="text-gray-500 text-lg">No image selected.</p>
        <button
          onClick={() => navigate("/")}
          className="bg-indigo-600 text-white px-6 py-3 rounded-full hover:bg-indigo-700 transition"
        >
          Go back home
        </button>
      </div>
    );
  }

  return (
    <div className="max-w-4xl mx-auto px-4 py-12 font-['Outfit']">
      <h2 className="text-3xl font-bold text-gray-900 mb-2 text-center">
        Background Removed
      </h2>
      <p className="text-gray-500 text-center mb-8">
        Credits remaining: <span className="font-semibold text-indigo-600">{credits ?? "—"}</span>
      </p>

      {/* Comparison slider */}
      <div className="relative w-full overflow-hidden rounded-2xl shadow-lg mb-8 bg-checkered">
        {isProcessing ? (
          <div className="flex items-center justify-center h-80 bg-gray-100">
            <div className="flex flex-col items-center gap-3">
              <div className="w-10 h-10 border-4 border-indigo-500 border-t-transparent rounded-full animate-spin" />
              <p className="text-gray-500">Removing background…</p>
            </div>
          </div>
        ) : resultImage ? (
          <>
            <img
              src={originalImage}
              alt="original"
              className="w-full object-cover"
              style={{ clipPath: `inset(0 ${100 - sliderPosition}% 0 0)` }}
            />
            <img
              src={resultImage}
              alt="result"
              className="absolute top-0 left-0 w-full h-full object-cover"
              style={{ clipPath: `inset(0 0 0 ${sliderPosition}%)` }}
            />
            <input
              type="range"
              min={2}
              max={98}
              value={sliderPosition}
              onChange={(e) => setSliderPosition(Number(e.target.value))}
              className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-full z-10 slider"
            />
          </>
        ) : (
          <img src={originalImage} alt="original" className="w-full object-cover" />
        )}
      </div>

      {/* Action buttons */}
      <div className="flex flex-wrap justify-center gap-4">
        <label
          htmlFor="upload-new"
          className="cursor-pointer border border-gray-300 text-gray-700 font-medium px-6 py-3 rounded-full hover:bg-gray-50 transition"
        >
          Try another image
          <input
            id="upload-new"
            type="file"
            accept="image/*"
            hidden
            onChange={handleNewImage}
          />
        </label>

        <button
          onClick={handleDownload}
          disabled={!resultImage || isProcessing}
          className="bg-indigo-600 text-white font-medium px-6 py-3 rounded-full hover:bg-indigo-700 transition disabled:opacity-50 disabled:cursor-not-allowed"
        >
          Download PNG
        </button>
      </div>
    </div>
  );
};

export default Result;
