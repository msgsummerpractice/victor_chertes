type DogApiResponse = {
    message: string;
    status: string;
}

const button = document.getElementById('fetch-btn') as HTMLButtonElement;
const image = document.getElementById('dog-image') as HTMLImageElement;
const loadingText = document.getElementById('loading') as HTMLParagraphElement;
const errorText = document.getElementById('error') as HTMLParagraphElement;

async function getDogImage() : Promise<void> {
    image.style.display = 'none';
    errorText.style.display = 'none';
    loadingText.style.display = 'block';

    try {
        const response = await fetch('https://dog.ceo/api/breeds/image/random');
        const data = await response.json() as DogApiResponse;

        image.src = data.message;

        loadingText.style.display = 'none';
        image.style.display = 'block';
    } catch (error) {
        console.error('Fetch failed:', error);

        loadingText.style.display = 'none';
        errorText.style.display = 'block';
    }
}

button.addEventListener('click',getDogImage);