# This Dockerfile creates a static build image for CI

FROM openjdk:8-jdk

# Just matched `app/build.gradle`
ENV ANDROID_COMPILE_SDK "29"
# Just matched `app/build.gradle`
ENV ANDROID_BUILD_TOOLS "29.0.3"
# Version from https://developer.android.com/studio/releases/sdk-tools
ENV ANDROID_SDK_TOOLS "6609375"

ENV ANDROID_SDK_ROOT /android-sdk-root
ENV PATH="${PATH}:/android-sdk-root/platform-tools/"

# install OS packages
RUN apt-get --quiet update --yes
RUN apt-get --quiet install --yes wget tar unzip lib32stdc++6 lib32z1
RUN wget --quiet --output-document=android-sdk.zip https://dl.google.com/android/repository/commandlinetools-linux-${ANDROID_SDK_TOOLS}_latest.zip
RUN unzip -d android-sdk-root android-sdk.zip
ENV ANDROID_HOME=$PWD/android-sdk-root
ENV PATH=$PATH:$PWD/android-sdk-root/platform-tools/
RUN mkdir -p ~/.android/
RUN touch ~/.android/repositories.cfg
RUN echo y | android-sdk-root/tools/bin/sdkmanager "platforms;android-${ANDROID_COMPILE_SDK}" >/dev/null
RUN echo y | android-sdk-root/tools/bin/sdkmanager "platform-tools" >/dev/null
RUN echo y | android-sdk-root/tools/bin/sdkmanager "build-tools;${ANDROID_BUILD_TOOLS}" >/dev/null
RUN echo y | android-sdk-root/tools/bin/sdkmanager "extra-android-m2repository" >/dev/null
RUN echo y | android-sdk-root/tools/bin/sdkmanager "extra-google-google_play_services" >/dev/null
RUN echo y | android-sdk-root/tools/bin/sdkmanager "extra-google-m2repository" >/dev/null
RUN chmod +x ./gradlew
  # temporarily disable checking for EPIPE error and use yes to accept all licenses
RUN set +o pipefail
RUN yes | android-sdk-root/tools/bin/sdkmanager --licenses
RUN set -o pipefail

# install FastLane
COPY Gemfile.lock .
COPY Gemfile .
RUN gem install bundle
RUN bundle install
