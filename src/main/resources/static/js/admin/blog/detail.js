// Required and Messages
$('#form-update-blog').validate({
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
    messages: {
        title: {
            required: "Title is empty"
        },
        content: {
            required: "Content is empty"
        },
        description: {
            required: "Description is empty"
        },
        status: {
            required: "Status is empty"
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

// Update blog
const titleEl = document.getElementById('title');
const contentEl = document.getElementById('content');
const descriptionEl = document.getElementById('description');
const statusEl = document.getElementById('status');

const btnUpdate = document.getElementById('btn-update');
btnUpdate.addEventListener('click', function () {
    if (!$('#form-update-blog').valid()) {
        return;
    }

    // Send data to sever
    const data = {
        title: titleEl.value,
        content: contentEl.value,
        description: descriptionEl.value,
        status: statusEl.value === 'true',
    }

    // Using axios to send data to server
    axios.put(`/api/admin/blogs/${blog.id}/update-blog`, data)
        .then(function (response) {
            toastr.success('Update success')
            setTimeout(function () { location.reload(); }, 500)
        })
        .catch(function (error) {
            toastr.error(error.response.data.message)
        })
})

// Delete blog
const btnDelete = document.getElementById('btn-delete')
btnDelete.addEventListener('click', function () {
    const isConfirm = confirm('Are you sure you want to delete this blog?')
    if (!isConfirm) {
        return
    }
    axios.delete(`/api/admin/blogs/${blog.id}/delete-blog`)
        .then(function (response) {
            toastr.success('Delete blog success')
            setTimeout(function () {
                window.location.href = '/admin/blogs/own-blogs'
            }, 1500)
        })
        .catch(function (error) {
            toastr.error(error.response.data.message)
        })
})

// Upload thumbnail
const imagePreview = document.getElementById('image-preview')
const imageInput = document.getElementById('image')

imageInput.addEventListener("change", (e) => {
    // Get file was chosen
    const file = e.target.files[0]

    // Create object form data to get data and send
    const formData = new FormData()
    formData.append('file', file)

    // Call api using axios
    axios.post(`/api/admin/blogs/${blog.id}/upload-thumbnail`, formData)
        .then(res => {
            // Show image is uploaded
            imagePreview.src = res.data

            toastr.success('Upload thumbnail success')
        })
        .catch(err => {
            toastr.error(err.response.data.message)
        })
})

// Delete thumbnail
const deleteThumbnail = (event, blogId) => {
    const isConfirm = confirm('Are you sure you want to delete this thumbnail?')
       if (!isConfirm) {
           return
       }
       axios.delete(`/api/admin/blogs/${blogId}/delete-thumbnail`)
           .then(res => {
               toastr.success('Delete thumbnail success')
               setTimeout(() => {
                   location.reload()
               }, 1500)
           })
           .catch(err => {
               toastr.error(err.response.data.message)
           })
}