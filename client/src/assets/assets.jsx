import logo from './logo.png';
import video_banner from './home-page-banner.mp4';
import people from "./people.png";
import people_org from './people-org.png';
import slide_icon from './slide_icon.svg';

export const assets = {
    logo,
    video_banner,
    people,
    people_org,
    slide_icon
}

const steps = [
    {
        step: "Step 1",
        title: "Select an image",
        description: (
            <>
                First, choose the image you want to remove background from by clicking on "Start from a photo'. <br />
                Your image format can be PNG or JPG. <br />
                We support all image dimensions.
            </>
        ),
    },

    {
        step: "Step 2",
        title: "Let magic remove the background",
        description: (
            <>
                Our tool automatically removes the background from your image. Next, you can choose a background color or keep it transparent.<br />
                Our most popular options are white and transparent backgrounds, but you can pick any color you like!
            </>
        ),
    },
    {
        step: "Step 3",
        title: "Download your image",
        description: (
            <>
                After selecting a new background color, download your photo and you're done! <br />
                You can also save your picture in the Photoroom App by creating an account.
            </>
        ),
    },
];

export const categories = ["People", "Products", "Animals", "Cars", "Graphics"];

export default steps;
