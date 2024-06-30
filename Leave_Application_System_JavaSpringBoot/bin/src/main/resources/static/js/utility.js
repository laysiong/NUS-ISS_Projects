  function truncateText(text, maxLength) {
      if (text.length > maxLength) {
          return text.substring(0, maxLength) + '...';
      }
      return text;
  }

  document.addEventListener("DOMContentLoaded", function() {
      document.querySelectorAll('.truncate').forEach(function(element) {
          var text = element.innerText;
          element.innerText = truncateText(text, 10);
      });
  });




