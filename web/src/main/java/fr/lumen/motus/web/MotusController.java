package fr.lumen.motus.web;

import fr.lumen.motus.*;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/")
public class MotusController {
    private final List<String> dictionary;

    public MotusController(@Dictionary List<String> dictionary) {
        this.dictionary = dictionary;
    }

    @POST
    @Path("solve")
    public SolveAnswer solve(@Valid SolveQuery query) {
        final Solver solver = new Solver(dictionary, query.getLength(), query.getFirst().charAt(0));
        query.getPropositions().forEach(p -> solver.addProposition(p.getWord(), p.getResult()));
        final WordAdviser adviser = new WordAdviser(solver);
        final Optional<Advise> advise = adviser.advise(query.getDepth());
        return advise.map(a -> new SolveAnswer(a.isSolved(), a.getWord())).orElse(new SolveAnswer(false, null));
    }

    @POST
    @Path("/match")
    public MatchAnswer match(@Valid MatchQuery query) {
        final Matcher matcher = new Matcher(query.getAnswer().trim().toUpperCase());
        return query.getPropositions().stream().map(String::trim).map(String::toUpperCase).map(matcher::result).collect(Collectors.toCollection(MatchAnswer::new));
    }

}
