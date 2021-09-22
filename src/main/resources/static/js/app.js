'use strict';

(function() {
  var VALID_URL_REGEX = new RegExp('^https?:\\/\\/'+ // protocol
    '((([a-z\\d]([a-z\\d-]*[a-z\\d])*)\\.)+[a-z]{2,}|'+ // domain name
    '((\\d{1,3}\\.){3}\\d{1,3}))'+ // OR ip (v4) address
    '(\\:\\d+)?(\\/[-a-z\\d%_.~+]*)*'+ // port and path
    '(\\?[;&a-z\\d%_.~+=-]*)?'+ // query string
    '(\\#[-a-z\\d_]*)?$','i'); // fragment locator

  function validUrl(url) {
    return !!VALID_URL_REGEX.test(url);
  }
  
  function Input() {
    var status = 'none'; // 'none' | 'correct' | 'wrong'
    var $box = document.querySelector('#input');
    var $input = $box.querySelector('input[name=url]');
    var $message = $box.querySelector('#message');
    var messageTimeoutId = null;
    
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

    function showMessage(msg) {
      if (messageTimeoutId) {
        clearTimeout(messageTimeoutId);
      }
      $message.style.display = 'none';
      $message.style.display = '';
      $message.textContent = msg;
      $message.classList.add('show');
      messageTimeoutId = setTimeout(function() {
        messageTimeoutId = null;
        $message.classList.remove('show');
      }, 3000);
    }

    $input.addEventListener('input', handleInput);
    $message.addEventListener('click', function() {
      $message.classList.remove('show');
    });

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
          $input.disabled = '';
        }
      },
      message: showMessage
    };
  }

  function Result(input) {
    var $result = document.querySelector('#result');
    var $url = $result.querySelector('#shorten-url');
    var $clipboard = $result.querySelector('#clipboard');
    var copyTimeoutId = null;

    function handleCopy() {
      if (copyTimeoutId) {
        clearTimeout(copyTimeoutId);
      }
      $clipboard.classList.add('copy');
      input.message('복사가 완료되었습니다');
      navigator.clipboard.writeText($url.textContent);
      copyTimeoutId = setTimeout(function() {
        $clipboard.classList.remove('copy');
        copyTimeoutId = null;
      }, 2000)
    }

    $clipboard.addEventListener('click', handleCopy);

    return {
      url: function(url) {
        $result.classList.add('show');
        $url.textContent = url;
      },
      hide: function() {
        $result.classList.remove('show');
      }
    };
  }

  function Form(input, result) {
    var processing = false;
    var $form = document.querySelector('#form');

    function handleSubmit(e) {
      e.preventDefault();
      if (processing) {
        return;
      }

      if (input.status() === 'correct') {
        processing = true;
        input.process(true);
        result.hide();
        setTimeout(function() {
          processing = false;
          input.process(false);
          input.message('주소가 성공적으로 줄여졌습니다 복사해서 사용하세요 :)');
          result.url('https://to2.kr/abc');
        }, 1000);
      }
    }

    $form.addEventListener('submit', handleSubmit);
  }

  function handleDocumentLoad() {
    var input = Input();
    var result = Result(input);
    Form(input, result);
  }

  window.addEventListener('load', handleDocumentLoad);
})();
