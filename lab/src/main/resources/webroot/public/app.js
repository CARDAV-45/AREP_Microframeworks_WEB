document.getElementById('form').addEventListener('submit', function(e) {
    e.preventDefault();
    const nombre = document.getElementById('nombre').value;
    fetch('/App/hello?name=' + encodeURIComponent(nombre))
        .then(r => r.text())
        .then(data => {
            document.getElementById('resultado').textContent = data;
            document.getElementById('resultado').classList.add('show');
        });
});
