toastr.options = {
  "closeButton": false,
  "debug": false,
  "newestOnTop": false,
  "progressBar": false,
  "positionClass": "toast-top-right",
  "preventDuplicates": false,
  "onclick": null,
  "showDuration": "300",
  "hideDuration": "1000",
  "timeOut": "5000",
  "extendedTimeOut": "1000",
  "showEasing": "swing",
  "hideEasing": "linear",
  "showMethod": "fadeIn",
  "hideMethod": "fadeOut"
}

// logout handle
function logout() {
    axios.post('/api/auth/logout')
        .then(function (response) {
             toastr.success("Logout success")
             setTimeout(function() { window.location.href = '/login' }, 1000);
        })
        .catch(function(error) {
            console.log(error)
            toastr.error(error.response.data.message)
        });
}

// active menu handle
function activeMenu() {
    // get path present
    let path =  window.location.pathname;
    // handle active menu
    const menuItems = document.querySelectorAll('#main-menu .navbar-nav .nav-item .nav-link')
    menuItems.forEach(function (menu) {
        if(menu.getAttribute('href') === path) {
            menu.classList.add('active')
        }
    })
}
activeMenu();