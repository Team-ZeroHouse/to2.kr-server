'use strict';

(function() {
  var VALID_URL_REGEX = new RegExp('^https?:\\/\\/'+ // protocol
    '((([a-z\\d]([a-z\\d-]*[a-z\\d])*)\\.)+[a-z]{2,}|'+ // domain name
    '((\\d{1,3}\\.){3}\\d{1,3}))'+ // OR ip (v4) address
    '(\\:\\d+)?(\\/[-a-z\\d%_.~+]*)*'+ // port and path
    '(\\?[;&a-z\\d%_.~+=-]*)?'+ // query string
    '(\\#[-a-z\\d_]*)?$','i'); // fragment locator

  var input = null;

  function validUrl(url) {
    return !!VALID_URL_REGEX.test(url);
  }
  
  function Input() {
    var status = 'none'; // 'none' | 'correct' | 'wrong'
    var $box = document.querySelector('#input');
    var $input = $box.querySelector('input[name=url]');
    
    function handleInput() {
      var url = $input.value;
      if (url.length === 0) {
        $box.classList.remove('correct', 'wrong');
        status = 'none';
        return;
      }

      if (validUrl(url)) {
        if (status === 'wrong') {
          $box.classList.remove('wrong');
        }
        if (status !== 'correct') {
          $box.classList.add('correct');
        }
        status = 'correct';
      } else {
        if (status === 'correct') {
          $box.classList.remove('correct');
        }
        if (status !== 'wrong') {
          $box.classList.add('wrong');
        }
        status = 'wrong';
      }
    }

    $input.addEventListener('input', handleInput);

    return {
      value: function() {
        return element.value;
      },
      status: function() {
        return status;
      },
      process: function(value) {
        if (value) {
          $box.classList.add('process');
          $input.disabled = 'disabled'
        } else {
          $box.classList.remove('process');
          delete $input.disabled;
        }
      },
      message: function(msg) {

      }
    }
  }

  function handleSubmit(e) {
    e.preventDefault();
    if (input.status() === 'correct') {
      input.process(true);
      setTimeout(function() {
        input.process(false);
        input.message('끝!');
      }, 3000);
    }
  }

  function handleDocumentLoad() {
    input = Input();
    document.querySelector('#form').addEventListener('submit', handleSubmit);
  }

  window.addEventListener('load', handleDocumentLoad);
})();
