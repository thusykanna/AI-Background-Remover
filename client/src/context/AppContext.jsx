import { createContext, useState, useCallback } from "react";
import axios from "axios";
import toast from "react-hot-toast";

export const AppContext = createContext();

const AppContextProvider = (props) => {
  const backendUrl = import.meta.env.VITE_BACKEND_URL;
  const [credits, setCredits] = useState(null);
  const [originalImage, setOriginalImage] = useState(null);
  const [resultImage, setResultImage] = useState(null);
  const [isProcessing, setIsProcessing] = useState(false);

  const loadUser = useCallback(async (getToken) => {
    try {
      const token = await getToken();
      const { data } = await axios.get(backendUrl + "/users/me", {
        headers: { Authorization: `Bearer ${token}` },
      });
      if (data.success) {
        setCredits(data.data.credits);
      }
    } catch (error) {
      console.error("Failed to load user credits", error);
    }
  }, [backendUrl]);

  const removeBg = useCallback(async (file, getToken) => {
    setOriginalImage(URL.createObjectURL(file));
    setResultImage(null);
    setIsProcessing(true);
    try {
      const token = await getToken();
      const formData = new FormData();
      formData.append("image", file);

      const { data } = await axios.post(backendUrl + "/images/remove-bg", formData, {
        headers: { Authorization: `Bearer ${token}` },
      });

      if (data.success) {
        setResultImage("data:image/png;base64," + data.data);
        setCredits((prev) => (prev !== null ? prev - 1 : null));
      } else {
        toast.error(data.data || "Background removal failed.");
      }
    } catch (error) {
      const msg = error.response?.data?.data || "Something went wrong. Please try again.";
      toast.error(msg);
    } finally {
      setIsProcessing(false);
    }
  }, [backendUrl]);

  const contextValue = {
    backendUrl,
    credits,
    setCredits,
    loadUser,
    originalImage,
    resultImage,
    isProcessing,
    removeBg,
  };

  return (
    <AppContext.Provider value={contextValue}>
      {props.children}
    </AppContext.Provider>
  );
};

export default AppContextProvider;
