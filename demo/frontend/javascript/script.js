// const button = document.getElementById('fetch-btn');
// const image = document.getElementById('dog-image');
// const loadingText = document.getElementById('loading');
// const errorText = document.getElementById('error');

// async function getDogImage() {
//     image.style.display = 'none';
//     errorText.style.display = 'none';
//     loadingText.style.display = 'block';

//     try {
//         const response = await fetch('https://dog.ceo/api/breeds/image/random');
//         const data = await response.json();
//         image.src = data.message;

//         loadingText.style.display = 'none';
//         image.style.display = 'block';
//     } catch (error) {
//         console.error('Fetch failed:', error);

//         loadingText.style.display = 'none';
//         errorText.style.display = 'block';
//     }
// }

// button.addEventListener('click',getDogImage);