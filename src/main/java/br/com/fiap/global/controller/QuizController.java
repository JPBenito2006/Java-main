package br.com.fiap.global.controller;

import br.com.fiap.global.excecao.ErrorMessage;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.fiap.global.dominio.Quiz;
import br.com.fiap.global.service.QuizService;

@Path("quizzes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class QuizController {
    
    private static final Logger logger = Logger.getLogger(QuizController.class.getName());
    
    private QuizService quizService;
    
    public QuizController() {
        this.quizService = new QuizService();
    }
    
    @POST
    public Response criarQuiz(Quiz quiz) {
        try {
            quizService.adicionarQuiz(quiz);
            logger.log(Level.INFO, "Quiz criado com ID: {0}", quiz.getId());
            return Response.status(Response.Status.CREATED).entity(quiz).build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao criar o quiz", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity(new ErrorMessage("Erro ao criar o quiz")).build();
        }
    }
    
    @GET
    public Response obterTodosQuizzes() {
        List<Quiz> quizzes = quizService.listarTodosQuizzes();
        logger.log(Level.INFO, "Obtidos {0} quizzes", quizzes.size());
        return Response.status(Response.Status.OK).entity(quizzes).build();
    }
    
    @GET
    @Path("/{id}")
    public Response obterQuizPorId(@PathParam("id") Long id) {
        Quiz quiz = quizService.buscarQuizPorId(id);
        if (quiz == null) {
            logger.log(Level.WARNING, "Quiz não encontrado para o ID: {0}", id);
            return Response.status(Response.Status.NOT_FOUND)
                           .entity(new ErrorMessage("Quiz não encontrado para o ID: " + id)).build();
        }
        logger.log(Level.INFO, "Quiz encontrado para o ID: {0}", id);
        return Response.status(Response.Status.OK).entity(quiz).build();
    }
    
    @PUT
    @Path("/{id}")
    public Response atualizarQuiz(@PathParam("id") Long id, Quiz quizAtualizado) {
        Quiz quizExistente = quizService.buscarQuizPorId(id);
        if (quizExistente == null) {
            logger.log(Level.WARNING, "Tentativa de atualização de quiz inexistente para o ID: {0}", id);
            return Response.status(Response.Status.NOT_FOUND)
                           .entity(new ErrorMessage("Quiz não encontrado para o ID: " + id)).build();
        }
        try {
            quizAtualizado.setId(id);
            quizService.atualizarQuiz(quizAtualizado);
            logger.log(Level.INFO, "Quiz atualizado para o ID: {0}", id);
            return Response.status(Response.Status.OK).entity(quizAtualizado).build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao atualizar o quiz para o ID: " + id, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity(new ErrorMessage("Erro ao atualizar o quiz")).build();
        }
    }
    
    @DELETE
    @Path("/{id}")
    public Response removerQuiz(@PathParam("id") Long id) {
        Quiz quiz = quizService.buscarQuizPorId(id);
        if (quiz == null) {
            logger.log(Level.WARNING, "Tentativa de remoção de quiz inexistente para o ID: {0}", id);
            return Response.status(Response.Status.NOT_FOUND)
                           .entity(new ErrorMessage("Quiz não encontrado para o ID: " + id)).build();
        }
        try {
            quizService.removerQuiz(id);
            logger.log(Level.INFO, "Quiz removido para o ID: {0}", id);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao remover o quiz para o ID: " + id, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity(new ErrorMessage("Erro ao remover o quiz")).build();
        }
    }
    
    // Endpoint para adicionar uma pergunta a um quiz específico
    @POST
    @Path("/{quizId}/perguntas")
    public Response adicionarPergunta(@PathParam("quizId") Long quizId, 
                                      br.com.fiap.global.dominio.Question pergunta) {
        try {
            quizService.adicionarPerguntaAoQuiz(quizId, pergunta);
            logger.log(Level.INFO, "Pergunta adicionada ao quiz ID: {0}", quizId);
            return Response.status(Response.Status.CREATED).entity(pergunta).build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao adicionar pergunta ao quiz ID: " + quizId, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Erro ao adicionar pergunta ao quiz").build();
        }
    }
    
    // Endpoint para remover uma pergunta de um quiz específico
    @DELETE
    @Path("/{quizId}/perguntas/{perguntaId}")
    public Response removerPergunta(@PathParam("quizId") Long quizId, 
                                    @PathParam("perguntaId") Long perguntaId) {
        try {
            quizService.removerPerguntaDoQuiz(quizId, perguntaId);
            logger.log(Level.INFO, "Pergunta ID: {0} removida do quiz ID: {1}", new Object[]{perguntaId, quizId});
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao remover a pergunta ID: " + perguntaId + " do quiz ID: " + quizId, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Erro ao remover pergunta do quiz").build();
        }
    }
}