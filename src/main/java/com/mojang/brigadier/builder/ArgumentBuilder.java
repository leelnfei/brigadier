package com.mojang.brigadier.builder;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.RootCommandNode;

import java.util.Collection;
import java.util.function.Predicate;

public abstract class ArgumentBuilder<S, T extends ArgumentBuilder<S, T>> {
    private final RootCommandNode<S> arguments = new RootCommandNode<>();
    private Command<S> command;
    private Predicate<S> requirement = s -> true;
    private CommandNode<S> target;

    protected abstract T getThis();

    public T then(final ArgumentBuilder<S, ?> argument) {
        if (target != null) {
            throw new IllegalStateException("Cannot add children to a redirected node");
        }
        arguments.addChild(argument.build());
        return getThis();
    }

    public Collection<CommandNode<S>> getArguments() {
        return arguments.getChildren();
    }

    public T executes(final Command<S> command) {
        this.command = command;
        return getThis();
    }

    public Command<S> getCommand() {
        return command;
    }

    public T requires(final Predicate<S> requirement) {
        this.requirement = requirement;
        return getThis();
    }

    public Predicate<S> getRequirement() {
        return requirement;
    }

    public T redirect(final CommandNode<S> target) {
        if (!arguments.getChildren().isEmpty()) {
            throw new IllegalStateException("Cannot redirect a node with children");
        }
        this.target = target;
        return getThis();
    }

    public CommandNode<S> getRedirect() {
        return target;
    }

    public abstract CommandNode<S> build();
}
