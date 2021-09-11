$(function(){
   $("#ac-menu div").on("click", function() {
      $(this).next().slideToggle();
      $(this).toggleClass("active");
   });
});

