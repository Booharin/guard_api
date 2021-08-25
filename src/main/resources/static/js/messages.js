$( document ).ready(function() {

   function showMessages()
   {
     let token = sessionStorage.getItem("token");
     $(".body-table").html("");

     let fromdate = $("#fromdate").val();
     let todate = $("#todate").val();

     $.ajax({
     url: URL + "admin/messages?fromdate="+fromdate+"&todate="+todate,
     type: 'GET',
     beforeSend: function (xhr) {
         xhr.setRequestHeader('Authorization', `Bearer_${token}`);
     },
     data: {},
     success: function (data) {
        data.messages.forEach((item) => {
          $(".body-table").append("<tr><td>"+item.chatId+"</td><td>"+item.dateCreated.substring(0,19)+"</td><td>"+item.senderName+"</td><td>"+item.senderId+"</td><td>"+item.content+"</td></tr>");
        })
     },
     error: function () {
        alert("error");
     },
     });
   }

   $( "#formsearch" ).submit(function( event ) {
     event.preventDefault();
     showMessages();
   });

});