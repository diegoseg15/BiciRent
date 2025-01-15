/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/**/*.{html,ts}"],
  theme: {
    extend: {
      colors: {
        primary:"#fd7f51",
        matPrimary: '#3f51b5', 
      },

    },
  },
  plugins: [
    require('tailwindcss'),
    require('autoprefixer'),
  ],
}

