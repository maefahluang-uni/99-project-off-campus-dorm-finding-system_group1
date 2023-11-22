
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




//form submit
const searchbtn = document.querySelector(".searchbtn")
searchbtn.addEventListener("click", ()=>{
document.getElementById('searchForm').submit();
})



