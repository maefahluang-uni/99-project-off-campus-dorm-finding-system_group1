
const section = document.querySelector("section")
// pop-up for booking
const book = document.querySelectorAll(".book")
const overlay1 = document.querySelector(".overlay1")
const cancel1 = document.querySelector(".cancel1")
book.forEach(booking => {
booking.addEventListener("click", () => section.classList.add("popup"))
overlay1.addEventListener("click", () => section.classList.remove("popup"))
cancel1.addEventListener("click", () => section.classList.remove("popup"))
})

//alert message
const bookbtn = document.querySelector(".booking")
const okay = document.querySelector(".ok")

bookbtn.addEventListener("click", ()=>  {
    section.classList.add("alert")
    section.classList.remove("popup")
})
okay.addEventListener("click", () => section.classList.remove("alert"))
overlay1.addEventListener("click", () => section.classList.remove("alert"))

//form submit
const searchbtn = document.querySelector(".searchbtn")
searchbtn.addEventListener("click", ()=>{
document.getElementById('searchForm').submit();
})



