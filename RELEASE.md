# Releases

This page describes the release process and the currently planned schedule for upcoming releases as well as the respective release shepherd.

## Release schedule

| release series | date  (year-month-day) | release shepherd                            |
|----------------|--------------------------------------------|---------------------------------------------|
| v0.1.0           | 2022-12-01                                 | Hongzhou Wei (GitHub: @hongzhouzi) |
|
# How to cut a new release

> This guide is strongly based on the [Prometheus release instructions](https://github.com/prometheus/prometheus/blob/master/RELEASE.md).

## Branch management and versioning strategy

We use [Semantic Versioning](http://semver.org/).

We maintain a separate branch for each minor release, named `release-<major>.<minor>`, e.g. `release-1.1`, `release-2.0`.

The usual flow is to merge new features and changes into the master branch and to merge bug fixes into the latest release branch. Bug fixes are then merged into master from the latest release branch. The master branch should always contain all commits from the latest release branch.

If a bug fix got accidentally merged into master, cherry-pick commits have to be created in the latest release branch, which then have to be merged back into master. Try to avoid that situation.

Maintaining the release branches for older minor releases happens on a best effort basis.

## Prepare your release

For a new major or minor release, work from the `main` branch. For a patch release, work in the branch of the minor release you want to patch (e.g. `release-0.1` if you're releasing `v0.1.1`).

Add an entry for the new version to the `CHANGELOG.md` file. Entries in the `CHANGELOG.md` should be in this order:

* `[CHANGE]`
* `[FEATURE]`
* `[ENHANCEMENT]`
* `[BUGFIX]`

Create a PR for the changes to be reviewed.

## Publish the new release

For new minor and major releases, create the `release-<major>.<minor>` branch starting at the PR merge commit.
From now on, all work happens on the `release-<major>.<minor>` branch.

Bump the version in the `VERSION` file in the root of the repository.

Bump the version in properties.revision of the parent pom file.

Tag the new release with a tag named `v<major>.<minor>.<patch>`, e.g. `v2.1.3`. Note the `v` prefix. You can do the tagging on the commandline:

```bash
tag="$(< VERSION)"
git tag -a "${tag}" -m "${tag}"
git push origin "${tag}"
```
Commit all the changes.

The corresponding container image will be built and pushed to [docker hub](registry.cn-beijing.aliyuncs.com/kse/spring-cloud-gateway) automatically by CI once the release tag is added.



Finally, create a new release:

- Go to https://github.com/kubesphere-sigs/spring-cloud-gateway/releases/new.
- Associate the new release with the previously pushed tag.
- Add release notes based on `CHANGELOG.md`.

For patch releases, cherry-pick the commits from the release branch into the master branch.