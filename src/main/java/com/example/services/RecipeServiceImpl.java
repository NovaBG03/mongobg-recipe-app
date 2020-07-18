package com.example.services;

import com.example.commands.RecipeCommand;
import com.example.converters.RecipeCommandToRecipe;
import com.example.converters.RecipeToRecipeCommand;
import com.example.domain.Ingredient;
import com.example.domain.Recipe;
import com.example.exceptions.NotFoundException;
import com.example.repositories.RecipeRepository;
import com.example.repositories.reactive.RecipeReactiveRepository;
import com.example.repositories.reactive.UnitOfMeasureReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeReactiveRepository recipeReactiveRepository;
    private final UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;
    private final RecipeCommandToRecipe recipeCommandToRecipe;
    private final RecipeToRecipeCommand recipeToRecipeCommand;

    public RecipeServiceImpl(RecipeReactiveRepository recipeReactiveRepository,
                             UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository,
                             RecipeCommandToRecipe recipeCommandToRecipe, RecipeToRecipeCommand recipeToRecipeCommand) {
        this.recipeReactiveRepository = recipeReactiveRepository;
        this.unitOfMeasureReactiveRepository = unitOfMeasureReactiveRepository;
        this.recipeCommandToRecipe = recipeCommandToRecipe;
        this.recipeToRecipeCommand = recipeToRecipeCommand;
    }


    @Override
    public Flux<Recipe> getRecipes() {
        log.debug("I'm in the service");

        return recipeReactiveRepository.findAll();
    }

    @Override
    public Mono<Recipe> findById(String id) {

        Mono<Recipe> recipeMono = recipeReactiveRepository.findById(id);

        if (recipeMono.block() == null) {
            throw new NotFoundException("Recipe Not Found. For ID value: " + id );
        }

        return recipeMono;
    }

    @Override
    public Mono<RecipeCommand> findCommandById(String id) {

        return this.findById(id).map(recipe -> recipeToRecipeCommand.convert(recipe));
    }

    @Override
    public Mono<RecipeCommand> saveRecipeCommand(RecipeCommand command) {
        Recipe detachedRecipe = recipeCommandToRecipe.convert(command);

        for (Ingredient ingredient : detachedRecipe.getIngredients()) {
            ingredient.setUom(unitOfMeasureReactiveRepository.findById(ingredient.getUom().getId()).block());
        }

        Mono<Recipe> savedRecipe = recipeReactiveRepository.save(detachedRecipe);
        log.debug("Saved RecipeId:" + savedRecipe.block().getId());

        return savedRecipe.map(recipe -> recipeToRecipeCommand.convert(recipe));
    }

    @Override
    public Mono<Void> deleteById(String idToDelete) {
        return recipeReactiveRepository.deleteById(idToDelete);
    }
}
