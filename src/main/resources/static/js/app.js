'use strict';

(function() {
  var VALID_URL_REGEX = new RegExp('^https?:\\/\\/'+ // protocol
    '((([a-z\\d]([a-z\\d-]*[a-z\\d])*)\\.)+[a-z]{2,}|'+ // domain name
    '((\\d{1,3}\\.){3}\\d{1,3}))'+ // OR ip (v4) address
    '(\\:\\d+)?(\\/[-a-z\\d%_.~+]*)*'+ // port and path
    '(\\?[;&a-z\\d%_.~+=-]*)?'+ // query string
    '(\\#[-a-z\\d_]*)?$','i'); // fragment locator

  // polyfill
  Number.isInteger = Number.isInteger || function(value) {
    return typeof value === 'number' &&
      isFinite(value) &&
      Math.floor(value) === value;
  };

  function validUrl(url) {
    return !!VALID_URL_REGEX.test(url);
  }

  function Figure() {
    var animateTimeoutId = null;
    var $figure = document.querySelector('#figure');

    return {
      done: function(value) {
        if (animateTimeoutId) {
          clearTimeout(animateTimeoutId);
          animateTimeoutId = null;
        }
        $figure.classList.add('animate');
        animateTimeoutId = setTimeout(function() {
          $figure.classList.remove('animate');
          animateTimeoutId = null;
        }, 1000);
        if (value) {
          $figure.classList.add('done');
        } else {
          $figure.classList.remove('done');
        }
      }
    }
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
        $box.classList.remove('correct');
        $box.classList.remove('wrong');
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

    function showMessage(msg, type, time) {
      if (messageTimeoutId) {
        clearTimeout(messageTimeoutId);
        messageTimeoutId = null;
      }
      $message.style.display = 'none';
      $message.style.display = '';
      $message.textContent = msg;
      if (type !== 'success' && type !== 'error') {
        type = 'success';
      }
      $message.classList.remove('success');
      $message.classList.remove('error');
      $message.classList.add('show', type);
      $message.classList.add(type);
      if (!Number.isInteger(time)) {
        time = 3000;
      }
      messageTimeoutId = setTimeout(function() {
        messageTimeoutId = null;
        $message.classList.remove('show');
      }, time);
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
        copyTimeoutId = null;
      }
      $clipboard.setAttribute('class', 'copy');
      input.message('복사가 완료되었습니다');
      if (navigator.clipboard) {
        navigator.clipboard.writeText($url.textContent);
      } else {
        window.clipboardData.setData("Text", $url.textContent);
      }
      copyTimeoutId = setTimeout(function() {
        $clipboard.setAttribute('class', '');
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

  function Form(input, result, figure) {
    var processing = false;
    var $form = document.querySelector('#form');
    var $recapchaInput = $form.querySelector('input[name=recaptcha]');
    var $urlInput = $form.querySelector('input[name=url]');
    var $script = document.querySelector('#recaptcha');
    var recaptchaKey = null;

    if ($script) {
      recaptchaKey = $script.src.match(/\?render=(.+)$/)[1];
    }

    function beforeSend() {
      processing = true;
      if ($recapchaInput) {
        $recapchaInput.value = '';
      }
      input.process(true);
      figure.done(false);
      result.hide();
    }

    function afterReceive(url, message, error) {
      processing = false;
      input.process(false);
      if (error) {
        input.message(message, 'error')
      } else {
        input.message(message);
        result.url(url);
        figure.done(true);
      }
    }

    function getData() {
      var data = {};
      data.url = $urlInput.value.trim();
      if (recaptchaKey) {
        data.recaptcha = $recapchaInput.value;
      }
      return data;
    }

    function handleSubmit(e) {
      e.preventDefault();
      if (processing) {
        return;
      }

      if (input.status() === 'correct') {
        beforeSend();
        // grecaptcha로 Promise의 polyfill이 돼서 별도의 처리를 하지 않아도 된다.
        new Promise(function(resolve) {
          if (recaptchaKey) {
            grecaptcha.ready(function() {
              grecaptcha.execute(recaptchaKey, { action: 'submit' }).then(function(token) {
                $recapchaInput.value = token;
                resolve();
              });
            });
          } else {
            resolve();
          }
        }).catch(function() {
          afterReceive(null, '로봇인증에 오류가 발생했습니다.', true);
        }).then(function() {
          return new Promise(function(resolve, reject) {
            var data = getData();
            axios.post('/shorten', data).then(function(res) {
              var shortenUrl = res.data.shortenUrl;
              resolve(shortenUrl);
            }, function(e) {
              var res = e.response;
              if (res.data.error) {
                if (res.data.code === 'RECAPTCHA_INVALID') {
                  return reject({ type: 'to2.kr-error', message: '사용자 로봇 검증에 에러가 발생했습니다.' });
                }
              }
              
              return reject(e);
            }).catch(function(e) {
              reject(e);
            });
          });
        }).then(function(url) {
          afterReceive(url, '주소가 성공적으로 줄여졌습니다 복사해서 사용하세요 :)');
        }).catch(function(e) {
          if (e.type === 'to2.kr-error') {
            afterReceive(null, e.message, true);
          } else {
            throw e;
          }
        }).catch(function(e) {
          afterReceive(null, '알 수 없는 에러가 발생했습니다.', true);
          console.error('처리 불가한 에러', e);
        });
      }
    }

    $form.addEventListener('submit', handleSubmit);
  }

  function handleDocumentLoad() {
    var figure = Figure();
    var input = Input();
    var result = Result(input);
    Form(input, result, figure);
  }

  window.addEventListener('load', handleDocumentLoad);
})();
