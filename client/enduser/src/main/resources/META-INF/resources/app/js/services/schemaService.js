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

angular.module('self')
        .factory('SchemaService', ['$resource', '$q', '$http',
          function ($resource, $q, $http) {

            var schemaService = {};

            schemaService.getUserSchemas = function (anyTypeClass) {

              var classParam = anyTypeClass ? "?anyTypeClass=" + encodeURI(anyTypeClass) : "";

              return  $http.get("/syncope-enduser/api/schemas" + classParam)
                      .then(function (response) {
                        return response.data;
                      }, function (response) {
                        console.error("Something went wrong during schema retrieval, exit with status: ", response);
                        return $q.reject(response.data || response.statusText);
                      });
            };
            return schemaService;
          }]);


