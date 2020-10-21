# This Dockerfile creates a static build image for CI

FROM openjdk:8-jdk

# Just matched `app/build.gradle`
ENV ANDROID_COMPILE_SDK "29"
# Just matched `app/build.gradle`
ENV ANDROID_BUILD_TOOLS "29.0.3"
# Version from https://developer.android.com/studio/releases/sdk-tools
ENV ANDROID_SDK_TOOLS "6609375"

ENV ANDROID_SDK_ROOT /android-sdk-root
ENV PATH="${PATH}:${ANDROID_SDK_ROOT}/platform-tools/:$ANDROID_SDK_ROOT/cmdline-tools/tools/bin/"

# install OS packages
RUN apt-get --quiet update --yes
RUN apt-get --quiet install --yes wget tar unzip lib32stdc++6 lib32z1
RUN wget --quiet --output-document=android-sdk.zip https://dl.google.com/android/repository/commandlinetools-linux-${ANDROID_SDK_TOOLS}_latest.zip
RUN mkdir -p ~/.android/ $ANDROID_SDK_ROOT/cmdline-tools
RUN touch ~/.android/repositories.cfg
RUN unzip -d $ANDROID_SDK_ROOT/cmdline-tools android-sdk.zip
RUN yes | sdkmanager "platforms;android-${ANDROID_COMPILE_SDK}"
RUN yes | sdkmanager "platform-tools"
RUN yes | sdkmanager "build-tools;${ANDROID_BUILD_TOOLS}"
# RUN yes | sdkmanager "extra-android-m2repository"
# RUN yes | sdkmanager "extra-google-google_play_services"
# RUN yes | sdkmanager "extra-google-m2repository"
# RUN chmod +x ./gradlew
  # temporarily disable checking for EPIPE error and use yes to accept all licenses
RUN set +o pipefail
RUN yes | sdkmanager --licenses
RUN set -o pipefail

# install FastLane
COPY Gemfile.lock .
COPY Gemfile .
RUN gem install bundle
RUN bundle install
