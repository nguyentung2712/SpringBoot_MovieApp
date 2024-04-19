$('#form-create-blog').validate({

    // required data for blog
    rules: {
        title: {
            required: true
        },
        content: {
            required: true
        },
        description: {
            required: true
        },
        status: {
            required: true
        }
    },

    // messages announce when blog's data is empty
    messages: {
        title: {
            required: "Title is empty!"
        },
        content: {
            required: "Content is empty!"
        },
        description: {
            required: "Description is empty!"
        },
        status: {
            required: "Status is empty!"
        }
    },
    errorElement: 'span',
    errorPlacement: function (error, element) {
        error.addClass('invalid-feedback');
        element.closest('.form-group').append(error);
    },
    highlight: function (element, errorClass, validClass) {
        $(element).addClass('is-invalid');
    },
    unhighlight: function (element, errorClass, validClass) {
        $(element).removeClass('is-invalid');
    }
});

// Create blog
const titleEl = document.getElementById('title');
const contentEl = document.getElementById('content');
const descriptionEl = document.getElementById('description');
const statusEl = document.getElementById('status');
const btnCreate = document.getElementById('btn-create');
btnCreate.addEventListener('click', function () {

    // if form-create-blog is not filled full => return
    if (!$('#form-create-blog').valid()) {
        return;
    }

    // Send blog's data to database
    const data = {
        title: titleEl.value,
        content: contentEl.value,
        description: descriptionEl.value,
        status: statusEl.value === 'true',
    }

    // Using axios to send data to server
    axios.post('/api/admin/blogs/create-blog', data)

        // announce after send data
        .then(function (response) {

            // announce when create success
            toastr.success('Create blog success')
            setTimeout(function () { window.location.href = `/admin/blogs/${response.data.id}/detail` }, 1500)
        })
        .catch(function (error) {

            // send error announce when create fail
            console.log(error)
            toastr.error(error.response.data.message)
        })
})