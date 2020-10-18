# This Dockerfile creates a static build image for CI

FROM openjdk:8-jdk

# Just matched `app/build.gradle`
ENV ANDROID_COMPILE_SDK "29"
# Just matched `app/build.gradle`
ENV ANDROID_BUILD_TOOLS "29.0.3"
# Version from https://developer.android.com/studio/releases/sdk-tools
ENV ANDROID_SDK_TOOLS "6609375"

# install OS packages
RUN apt-get --quiet update --yes
RUN apt-get --quiet install --yes wget tar unzip lib32stdc++6 lib32z1
RUN export ANDROID_SDK_ROOT=${PWD}android-sdk-root
RUN wget --quiet --output-document=$ANDROID_SDK_ROOT/cmdline-tools.zip https://dl.google.com/android/repository/commandlinetools-linux-${ANDROID_SDK_TOOLS}_latest.zip
RUN cd $ANDROID_SDK_ROOT
RUN unzip -d cmdline-tools cmdline-tools.zip
RUN export PATH=$PATH:${ANDROID_SDK_ROOT}/cmdline-tools/tools/bin/
RUN sdkmanager --version
RUN sdkmanager --sdk_root=${ANDROID_HOME} "platforms;android-${ANDROID_COMPILE_SDK}"
RUN sdkmanager --sdk_root=${ANDROID_HOME} "platform-tools"
RUN sdkmanager --sdk_root=${ANDROID_HOME} "build-tools;${ANDROID_BUILD_TOOLS}"
RUN sdkmanager --sdk_root=${ANDROID_HOME} "extra-android-m2repository"
RUN sdkmanager --sdk_root=${ANDROID_HOME} "extra-google-google_play_services"
RUN sdkmanager --sdk_root=${ANDROID_HOME} "extra-google-m2repository"
RUN export PATH=$PATH:${ANDROID_HOME}/platform-tools/
RUN chmod +x ./gradlew
# temporarily disable checking for EPIPE error and use yes to accept all licenses
RUN set +o pipefail
RUN yes | android-sdk-linux/tools/bin/sdkmanager --licenses
RUN set -o pipefail

# install FastLane
COPY Gemfile.lock .
COPY Gemfile .
RUN gem install bundle
RUN bundle install
