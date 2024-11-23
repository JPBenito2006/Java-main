package br.com.fiap.global.controller;

import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.fiap.global.dominio.Quiz;

public class QuizControllerTest {
    
    private QuizController controller;
    
    @BeforeEach
    void setUp() {
        controller = new QuizController();
    }
    
    @Test
    void criarEObterQuiz() {
        Quiz quiz = new Quiz();
        quiz.setTitulo("Sustentabilidade de Energia");
        quiz.setDescricao("Quiz sobre práticas sustentáveis na energia elétrica.");
        
        Response resposta = controller.criarQuiz(quiz);
        assertEquals(201, resposta.getStatus());
        Quiz quizCriado = (Quiz) resposta.getEntity();
        assertNotNull(quizCriado.getId());
        
        Response respostaObter = controller.obterQuizPorId(quizCriado.getId());
        assertEquals(200, respostaObter.getStatus());
        Quiz quizObtido = (Quiz) respostaObter.getEntity();
        assertEquals("Sustentabilidade de Energia", quizObtido.getTitulo());
    }
    
    @Test
    void atualizarQuizComId() {
        // Criar quiz inicial
        Quiz quiz = new Quiz();
        quiz.setTitulo("Quiz Inicial");
        quiz.setDescricao("Descrição inicial.");
        Response respostaCriar = controller.criarQuiz(quiz);
        assertEquals(201, respostaCriar.getStatus());
        Quiz quizCriado = (Quiz) respostaCriar.getEntity();
        assertNotNull(quizCriado.getId());

        // Atualizar quiz
        quizCriado.setTitulo("Quiz Atualizado");
        Response respostaAtualizar = controller.atualizarQuiz(quizCriado.getId(), quizCriado);
        assertEquals(200, respostaAtualizar.getStatus());
        Quiz quizAtualizado = (Quiz) respostaAtualizar.getEntity();
        assertEquals("Quiz Atualizado", quizAtualizado.getTitulo());
    }
}