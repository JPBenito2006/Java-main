package br.com.fiap.global.service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import br.com.fiap.global.dominio.Quiz;
import br.com.fiap.global.dominio.Question;
import br.com.fiap.global.dominio.RepositorioQuiz;
import br.com.fiap.global.infra.dao.QuizDAO;

public class QuizService {
    
    private static final Logger logger = Logger.getLogger(QuizService.class.getName());
    
    private RepositorioQuiz repositorioQuiz;
    
    public QuizService() {
        this.repositorioQuiz = new QuizDAO();
    }
    
    // Adicionar um novo quiz
    public void adicionarQuiz(Quiz quiz) {
        try {
            repositorioQuiz.adicionar(quiz);
            logger.log(Level.INFO, "Quiz adicionado com sucesso: {0}", quiz.getId());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao adicionar quiz", e);
            throw e;
        }
    }
    
    // Atualizar um quiz existente
    public void atualizarQuiz(Quiz quiz) {
        try {
            repositorioQuiz.atualizar(quiz);
            logger.log(Level.INFO, "Quiz atualizado com sucesso: {0}", quiz.getId());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao atualizar quiz: " + quiz.getId(), e);
            throw e;
        }
    }
    
    // Remover um quiz pelo ID
    public void removerQuiz(Long id) {
        try {
            repositorioQuiz.remover(id);
            logger.log(Level.INFO, "Quiz removido com sucesso: {0}", id);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao remover quiz: " + id, e);
            throw e;
        }
    }
    
    // Buscar um quiz pelo ID
    public Quiz buscarQuizPorId(Long id) {
        try {
            Quiz quiz = repositorioQuiz.buscarPorId(id);
            if (quiz != null) {
                logger.log(Level.INFO, "Quiz encontrado: {0}", id);
            } else {
                logger.log(Level.WARNING, "Quiz não encontrado: {0}", id);
            }
            return quiz;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao buscar quiz: " + id, e);
            throw e;
        }
    }
    
    // Listar todos os quizzes
    public List<Quiz> listarTodosQuizzes() {
        try {
            List<Quiz> quizzes = repositorioQuiz.listarTodos();
            logger.log(Level.INFO, "Listados {0} quizzes", quizzes.size());
            return quizzes;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao listar todos os quizzes", e);
            throw e;
        }
    }
    
    // Adicionar uma pergunta a um quiz específico
    public void adicionarPerguntaAoQuiz(Long quizId, Question pergunta) {
        try {
            Quiz quiz = repositorioQuiz.buscarPorId(quizId);
            if (quiz != null) {
                quiz.adicionarPergunta(pergunta);
                repositorioQuiz.atualizar(quiz);
                logger.log(Level.INFO, "Pergunta adicionada ao quiz ID: {0}", quizId);
            } else {
                logger.log(Level.WARNING, "Tentativa de adicionar pergunta a quiz inexistente ID: {0}", quizId);
                throw new IllegalArgumentException("Quiz não encontrado para o ID: " + quizId);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao adicionar pergunta ao quiz ID: " + quizId, e);
            throw e;
        }
    }
    
    // Remover uma pergunta de um quiz específico
    public void removerPerguntaDoQuiz(Long quizId, Long perguntaId) {
        try {
            Quiz quiz = repositorioQuiz.buscarPorId(quizId);
            if (quiz != null) {
                quiz.removerPergunta(perguntaId);
                repositorioQuiz.atualizar(quiz);
                logger.log(Level.INFO, "Pergunta ID: {0} removida do quiz ID: {1}", new Object[]{perguntaId, quizId});
            } else {
                logger.log(Level.WARNING, "Tentativa de remover pergunta de quiz inexistente ID: {0}", quizId);
                throw new IllegalArgumentException("Quiz não encontrado para o ID: " + quizId);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao remover pergunta ID: " + perguntaId + " do quiz ID: " + quizId, e);
            throw e;
        }
    }
}