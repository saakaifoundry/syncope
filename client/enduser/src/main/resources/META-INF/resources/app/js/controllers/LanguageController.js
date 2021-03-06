/* 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

'use strict';

angular.module('language')
        .controller('LanguageController', function ($scope) {

          $scope.languages = {
            availableLanguages: [
              {id: '1', name: 'Italiano'},
              {id: '2', name: 'English'},
              {id: '3', name: 'Portugese'}
            ],
            selectedLanguage: {id: '2', name: 'English'}
          };

          $scope.init = function () {
//            MainService.settings().then(function (response) {
//              $scope.mainSettings = response;
//            });

            console.debug("Init language controller");
          };

          $scope.changeLanguage = function (language) {

            console.info("Language changed to: ", language);
            
            $scope.languages.selectedLanguage = language;
            
//            $translate.use(langKey);
//            LanguageService.switchLocale.query({language: langKey}, {}, function (response) {
//              $scope.selectedLanguage.locale = langKey;
//            });
          };

          this.retrieveLanguages = function () {
//            LanguageService.language.query({}, function (response) {
//              $scope.languages = response;
//            });
            console.debug("Retrieving available languages");
          };


          this.retrieveLanguages();


        });

