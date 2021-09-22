(function() {
  function handleSubmit(e) {
    e.preventDefault();
  }

  function handleDocumentLoad() {
    document.querySelector('#form').addEventListener('submit', handleSubmit);
  }

  window.addEventListener('load', handleDocumentLoad);
})();
