jQuery(function ($) {
   $('#save').on('click', function () {
       var email = $('#email').val();
       var pass = $('#pass').val();
       yawp('/users').create({
           email : email,
           password : pass
       }).then(function (e) {
           console.log(e);
           $('#result').text('Success!');
       });
   });
});