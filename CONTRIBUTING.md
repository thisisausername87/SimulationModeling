# Contributing

This repository is intended for classroom use. Keep changes small, readable,
and easy for students to inspect.

## Before Submitting Work

Run the test suite:

```bash
ant test
```

For larger changes, run the full build:

```bash
ant all
```

To run every verification tool without rebuilding the Javadoc and JAR:

```bash
ant quality
```

## Coding Expectations

- Use clear names that match the simulation domain being modeled.
- Prefer simple control flow over clever shortcuts.
- Add or update tests when behavior changes.
- Keep generated files out of commits. The `build/` and `docs/` directories
  are created by Ant and should remain untracked.

## Suggested Student Workflow

1. Pull the latest version of the repository.
2. Run `ant test` before making changes.
3. Make one focused change.
4. Run `ant test` again.
5. Run `ant quality` before submitting larger work.
6. Commit with a short message that explains the behavior changed.
