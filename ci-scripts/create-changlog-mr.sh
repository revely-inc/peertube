#!/usr/bin/env bash

# External environment variables
VERSION_CODE=$VERSION_CODE
DEPLOY_KEY_ANDROID_REPO=$deploy_key_android_repo
API_TOKEN=$gitlab_api_access_token

# Skip the MR creation part if we are running on a CI skip commit
lastCommitDescription=$(git log -1 --no-merges --format=format:"%s")
if [[ "$lastCommitDescription" =~ ^\[(skip[[:space:]]ci|ci[[:space:]]skip)\].* ]]; then
  echo "Skipping MR creation because we saw [ci skip] or [skip ci] in the commit (we ignore merge commits)"
  exit;
fi

RELEASE_NOTES_BRANCH_NAME="docs/release-notes-$VERSION_CODE"

eval "$(ssh-agent -s)"
ssh-add <(echo "$DEPLOY_KEY_ANDROID_REPO")
mkdir -p ~/.ssh
echo -e "Host *\\n\\tStrictHostKeyChecking no\\n\\n" > ~/.ssh/config

git remote set-url origin "git@gitlab.com:$CI_PROJECT_PATH.git"
git config --global user.email "badger@gitter.im"
git config --global user.name "Gitter Badger"
git checkout master
git checkout -b "$RELEASE_NOTES_BRANCH_NAME"
git add "./fastlane/metadata/android/en-GB/changelogs/$VERSION_CODE.txt"
git commit -m "[skip ci] Add changelog for $VERSION_CODE !$CI_MERGE_REQUEST_IID"
git push --set-upstream origin "$RELEASE_NOTES_BRANCH_NAME"

# Extract the host where the server is running, and add the URL to the APIs
[[ $CI_PROJECT_URL =~ ^https?://[^/]+ ]] && HOST="${BASH_REMATCH[0]}/api/v4/projects/"

# Look which is the default branch
TARGET_BRANCH=master

# The description of our new MR, we want to remove the branch after the MR has
# been closed
BODY="{
    \"id\": ${CI_PROJECT_ID},
    \"source_branch\": \"${RELEASE_NOTES_BRANCH_NAME}\",
    \"target_branch\": \"${TARGET_BRANCH}\",
    \"remove_source_branch\": true,
    \"title\": \"Add changelog for ${VERSION_CODE}\",
    \"description\": \"This MR was automatically created from CI, ${CI_JOB_URL}\\n\\nWe copied \`CURRENT_VERSION.txt\` -> \`${VERSION_CODE}.txt\`\",
    \"assignee_id\":\"${GITLAB_USER_ID}\"
}";

# Require a list of all the merge request and take a look if there is already
# one with the same source branch
LISTMR=$(curl --silent "${HOST}${CI_PROJECT_ID}/merge_requests?state=opened" --header "PRIVATE-TOKEN:${API_TOKEN}");
COUNTBRANCHES=$(echo "$LISTMR" | grep -o "\"source_branch\":\"${SOURCE_BRANCH}\"" | wc -l);

# No MR found, let's create a new one
if [ "$COUNTBRANCHES" -eq "0" ]; then
    curl -X POST "${HOST}${CI_PROJECT_ID}/merge_requests" \
        --header "PRIVATE-TOKEN:${API_TOKEN}" \
        --header "Content-Type: application/json" \
        --data "${BODY}";

    echo "Opened a new merge request: WIP: ${SOURCE_BRANCH} and assigned to you";
    exit;
fi

echo "No new merge request opened";
