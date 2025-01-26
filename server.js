const express = require('express');
const bodyParser = require('body-parser');
const jwt = require('jsonwebtoken');
const bcrypt = require('bcryptjs');

// Configuração do servidor
const app = express();
const PORT = process.env.PORT || 3000; // Porta dinâmica para Render
const SECRET_KEY = process.env.SECRET_KEY || 'minha_chave_secreta'; // Suporte para variável de ambiente SECRET_KEY

// Middleware
app.use(bodyParser.json());

// Simulação de base de dados em memória
let users = [];

// Rota de registro
app.post('/register', async (req, res) => {
    const { username, password } = req.body;

    // Verificar se o utilizador já existe
    const userExists = users.find(user => user.username === username);
    if (userExists) {
        return res.status(400).json({ message: 'Usuário já registrado' });
    }

    // Encriptar a senha
    const hashedPassword = await bcrypt.hash(password, 10);

    // Adicionar utilizador ao "banco de dados"
    users.push({ username, password: hashedPassword });
    res.status(201).json({ message: 'Usuário registrado com sucesso' });
});

// Rota de login
app.post('/login', async (req, res) => {
    const { username, password } = req.body;

    // Verificar se o utilizador existe
    const user = users.find(user => user.username === username);
    if (!user) {
        return res.status(404).json({ message: 'Usuário não encontrado' });
    }

    // Verificar a senha
    const isPasswordValid = await bcrypt.compare(password, user.password);
    if (!isPasswordValid) {
        return res.status(401).json({ message: 'Senha inválida' });
    }

    // Gerar token JWT
    const token = jwt.sign({ username }, SECRET_KEY, { expiresIn: '1h' });
    res.status(200).json({ message: 'Login bem-sucedido', token });
});

// Iniciar o servidor
app.listen(PORT, () => {
    console.log(`Servidor rodando na porta ${PORT}`);
});
