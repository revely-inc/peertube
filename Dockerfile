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
ENV PATH ${PATH}:${ANDROID_SDK_ROOT}/platform-tools:${ANDROID_SDK_ROOT}/cmdline-tools/tools/bin
RUN yes | sdkmanager --licenses
RUN yes | sdkmanager "emulator" "platform-tools"
RUN yes | sdkmanager --update --channel=3
RUN touch /root/.android/repositories.cfg
RUN yes | sdkmanager "platforms;android-${ANDROID_COMPILE_SDK}"
RUN yes | sdkmanager "build-tools;${ANDROID_BUILD_TOOLS}"
RUN yes | sdkmanager "extra-android-m2repository"
RUN yes | sdkmanager "extra-google-google_play_services"
RUN yes | sdkmanager "extra-google-m2repository"
RUN chmod +x ./gradlew

# install FastLane
COPY Gemfile.lock .
COPY Gemfile .
RUN gem install bundle
RUN bundle install
