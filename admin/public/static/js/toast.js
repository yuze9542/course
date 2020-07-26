const Toast = Swal.mixin({
    toast: true,
    position: 'top-end',
    showConfirmButton: false,
    timer: 3000,
    timerProgressBar: true,
    onOpen: (toast) => {
        toast.addEventListener('mouseenter', Swal.stopTimer)
        toast.addEventListener('mouseleave', Swal.resumeTimer)
    }
})



toast = {
    success:function (msg) {
        Toast.fire({
            icon: 'success',
            title: msg
        })
    },
    error: function (msg) {
        Toast.fire({
            icon: 'error',
            title: msg
        })
    },
    warning: function (msg) {
        Toast.fire({
            icon: 'warning',
            title: msg
        })
    }
}