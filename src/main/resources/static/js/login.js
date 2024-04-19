// JS Login handle
const formLogin = document.getElementById('form-login');
const emailEl = document.getElementById('email');
const passwordEl = document.getElementById('password');
formLogin.addEventListener('submit', (e) => {
    e.preventDefault();
    const data = {
       email: emailEl.value,
       password: passwordEl.value
    }

    // Call api using axios
    axios.post('/api/auth/login', data)
        .then((response) => {
            console.log(response);
            if (response.status === 200) {
                toastr.success('Login success');
                setTimeout(() => {
                    window.location.href = '/';
                }, 1500)
            } else {
                toastr.error('Login fail');
            }
        })
        .catch((error) => {
            console.log(error);
            toastr.error(error.response.data.message);
        })
});