/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/**/*.{html,ts}"],
  theme: {
    extend: {
      colors: {
        primary:"rgba(6, 37, 51, 0.78)",
        matPrimary: '#3f51b5', // Cambia esto según el tema de Material
      },

    },
  },
  plugins: [
    require('tailwindcss'),
    require('autoprefixer'),
  ],
}

