/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/**/*.{html,ts}"],
  theme: {
    extend: {
      colors: {
        primary:"#fd7f51",
        matPrimary: '#3f51b5', // Cambia esto según el tema de Material
      },

    },
  },
  plugins: [
    require('tailwindcss'),
    require('autoprefixer'),
  ],
}

