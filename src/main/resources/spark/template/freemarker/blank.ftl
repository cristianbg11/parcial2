<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
    <title>Acortador</title>
    <link rel="stylesheet" href="assets/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.1/css/all.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="assets/fonts/fontawesome5-overrides.min.css">
    <link rel="stylesheet" href="assets/css/Data-Table-1.css">
    <link rel="stylesheet" href="assets/css/Data-Table.css">
    <link rel="stylesheet" href="https://cdn.datatables.net/1.10.15/css/dataTables.bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/3.5.2/animate.min.css">
    <link rel="stylesheet" href="assets/css/Pretty-Search-Form.css">
</head>

<body id="page-top">
    <div id="wrapper">
        <nav class="navbar navbar-dark align-items-start sidebar sidebar-dark accordion bg-gradient-primary p-0">
            <div class="container-fluid d-flex flex-column p-0">
                <a class="navbar-brand d-flex justify-content-center align-items-center sidebar-brand m-0" href="/index">
                    <div class="sidebar-brand-icon rotate-n-15"><i class="fas fa-laugh-wink"></i></div>
                    <div class="sidebar-brand-text mx-3"><span>Acortador</span></div>
                </a>
                <hr class="sidebar-divider my-0">
                <ul class="nav navbar-nav text-light" id="accordionSidebar">
                    <li class="nav-item" role="presentation"><a class="nav-link" href="/index"><i class="fas fa-tachometer-alt"></i><span>Dashboard</span></a></li>
                    <#if usuario?has_content>
                        <#if usuario.administrador == true>
                            <li class="nav-item" role="presentation"><a class="nav-link" href="/usuarios"><i class="fas fa-table"></i><span>Usuarios</span></a></li>
                            <li class="nav-item" role="presentation"><a class="nav-link" href="/urls"><i class="fas fa-window-maximize"></i><span>Urls</span></a></li>
                        </#if>
                    </#if>
                </ul>
                <div class="text-center d-none d-md-inline"><button class="btn rounded-circle border-0" id="sidebarToggle" type="button"></button></div>
            </div>
        </nav>
        <div class="d-flex flex-column" id="content-wrapper">
            <div id="content">
                <nav class="navbar navbar-light navbar-expand bg-white shadow mb-4 topbar static-top">
                    <div class="container-fluid"><button class="btn btn-link d-md-none rounded-circle mr-3" id="sidebarToggleTop" type="button"><i class="fas fa-bars"></i></button>
                        <form class="form-inline d-none d-sm-inline-block mr-auto ml-md-3 my-2 my-md-0 mw-100 navbar-search">
                            <div class="input-group"><input class="bg-light form-control border-0 small" type="text" placeholder="Search for ...">
                                <div class="input-group-append"><button class="btn btn-primary py-0" type="button"><i class="fas fa-search"></i></button></div>
                            </div>
                        </form>
                        <ul class="nav navbar-nav flex-nowrap ml-auto">
                            <li class="nav-item dropdown d-sm-none no-arrow"><a class="dropdown-toggle nav-link" data-toggle="dropdown" aria-expanded="false" href="#"><i class="fas fa-search"></i></a>
                                <div class="dropdown-menu dropdown-menu-right p-3 animated--grow-in" role="menu" aria-labelledby="searchDropdown">
                                    <form class="form-inline mr-auto navbar-search w-100">
                                        <div class="input-group"><input class="bg-light form-control border-0 small" type="text" placeholder="Search for ...">
                                            <div class="input-group-append"><button class="btn btn-primary py-0" type="button"><i class="fas fa-search"></i></button></div>
                                        </div>
                                    </form>
                                </div>
                            </li>
                            <li class="nav-item dropdown no-arrow mx-1" role="presentation">
                                <li class="nav-item dropdown no-arrow"><a class="dropdown-toggle nav-link" data-toggle="dropdown" aria-expanded="false" href="#"><span class="badge badge-danger badge-counter">3+</span><i class="fas fa-bell fa-fw"></i></a>
                                    <div class="dropdown-menu dropdown-menu-right dropdown-list dropdown-menu-right animated--grow-in"
                                        role="menu">
                                        <h6 class="dropdown-header">alerts center</h6>
                                        <a class="d-flex align-items-center dropdown-item" href="#">
                                            <div class="mr-3">
                                                <div class="bg-primary icon-circle"><i class="fas fa-file-alt text-white"></i></div>
                                            </div>
                                            <div><span class="small text-gray-500">December 12, 2019</span>
                                                <p>A new monthly report is ready to download!</p>
                                            </div>
                                        </a>
                                        <a class="d-flex align-items-center dropdown-item" href="#">
                                            <div class="mr-3">
                                                <div class="bg-success icon-circle"><i class="fas fa-donate text-white"></i></div>
                                            </div>
                                            <div><span class="small text-gray-500">December 7, 2019</span>
                                                <p>$290.29 has been deposited into your account!</p>
                                            </div>
                                        </a>
                                        <a class="d-flex align-items-center dropdown-item" href="#">
                                            <div class="mr-3">
                                                <div class="bg-warning icon-circle"><i class="fas fa-exclamation-triangle text-white"></i></div>
                                            </div>
                                            <div><span class="small text-gray-500">December 2, 2019</span>
                                                <p>Spending Alert: We've noticed unusually high spending for your account.</p>
                                            </div>
                                        </a><a class="text-center dropdown-item small text-gray-500" href="#">Show All Alerts</a></div>
                                </li>
                            </li>
                            <li class="nav-item dropdown no-arrow mx-1" role="presentation">
                                <li class="nav-item dropdown no-arrow"><a class="dropdown-toggle nav-link" data-toggle="dropdown" aria-expanded="false" href="#"><i class="fas fa-envelope fa-fw"></i><span class="badge badge-danger badge-counter">7</span></a>
                                    <div class="dropdown-menu dropdown-menu-right dropdown-list dropdown-menu-right animated--grow-in"
                                        role="menu">
                                        <h6 class="dropdown-header">alerts center</h6>
                                        <a class="d-flex align-items-center dropdown-item" href="#">
                                            <div class="dropdown-list-image mr-3"><img class="rounded-circle" src="assets/img/avatars/avatar4.jpeg">
                                                <div class="bg-success status-indicator"></div>
                                            </div>
                                            <div class="font-weight-bold">
                                                <div class="text-truncate"><span>Hi there! I am wondering if you can help me with a problem I've been having.</span></div>
                                                <p class="small text-gray-500 mb-0">Emily Fowler - 58m</p>
                                            </div>
                                        </a>
                                        <a class="d-flex align-items-center dropdown-item" href="#">
                                            <div class="dropdown-list-image mr-3"><img class="rounded-circle" src="assets/img/avatars/avatar2.jpeg">
                                                <div class="status-indicator"></div>
                                            </div>
                                            <div class="font-weight-bold">
                                                <div class="text-truncate"><span>I have the photos that you ordered last month!</span></div>
                                                <p class="small text-gray-500 mb-0">Jae Chun - 1d</p>
                                            </div>
                                        </a>
                                        <a class="d-flex align-items-center dropdown-item" href="#">
                                            <div class="dropdown-list-image mr-3"><img class="rounded-circle" src="assets/img/avatars/avatar3.jpeg">
                                                <div class="bg-warning status-indicator"></div>
                                            </div>
                                            <div class="font-weight-bold">
                                                <div class="text-truncate"><span>Last month's report looks great, I am very happy with the progress so far, keep up the good work!</span></div>
                                                <p class="small text-gray-500 mb-0">Morgan Alvarez - 2d</p>
                                            </div>
                                        </a>
                                        <a class="d-flex align-items-center dropdown-item" href="#">
                                            <div class="dropdown-list-image mr-3"><img class="rounded-circle" src="assets/img/avatars/avatar5.jpeg">
                                                <div class="bg-success status-indicator"></div>
                                            </div>
                                            <div class="font-weight-bold">
                                                <div class="text-truncate"><span>Am I a good boy? The reason I ask is because someone told me that people say this to all dogs, even if they aren't good...</span></div>
                                                <p class="small text-gray-500 mb-0">Chicken the Dog · 2w</p>
                                            </div>
                                        </a><a class="text-center dropdown-item small text-gray-500" href="#">Show All Alerts</a></div>
                                </li>
                                <div class="shadow dropdown-list dropdown-menu dropdown-menu-right" aria-labelledby="alertsDropdown"></div>
                            </li>
                            <div class="d-none d-sm-block topbar-divider"></div>
                            <li class="nav-item dropdown no-arrow" role="presentation">
                                <li class="nav-item dropdown no-arrow"><a class="dropdown-toggle nav-link" data-toggle="dropdown" aria-expanded="false" href="#"><span class="d-none d-lg-inline mr-2 text-gray-600 small">
                                        <#if usuario?has_content>
                                            ${usuario.username}
                                        </#if>
                                    </span><img class="border rounded-circle img-profile" src="assets/img/avatars/avatar1.jpeg"></a>
                                    <div
                                        class="dropdown-menu shadow dropdown-menu-right animated--grow-in" role="menu">
                                        <#if usuario.id != 1>
                                            <a class="dropdown-item" role="presentation" href="/perfil"><i class="fas fa-user fa-sm fa-fw mr-2 text-gray-400"></i>&nbsp;Perfil</a>
                                        </#if>
                                        <a class="dropdown-item" role="presentation"><i class="fas fa-cogs fa-sm fa-fw mr-2 text-gray-400"></i>&nbsp;Settings</a>
                                        <a
                                            class="dropdown-item" role="presentation" href="#"><i class="fas fa-list fa-sm fa-fw mr-2 text-gray-400"></i>&nbsp;Activity log</a>
                                            <div class="dropdown-divider"></div><a class="dropdown-item" role="presentation" href="/salir"><i class="fas fa-sign-out-alt fa-sm fa-fw mr-2 text-gray-400"></i>&nbsp;Salir</a></div>
                    </li>
                    </li>
                    </ul>
            </div>
            </nav>
            <div class="container-fluid">
                <h3 class="text-dark mb-1">Estadisticas</h3>
                <h5 class="text-dark mb-1">localhost:8080/r/${url.code}</h5>
            </div>
                <table id="example" class="table table-striped table-bordered" width="100%">
                    <thead>
                    <tr>
                        <th>Usuario</th>
                        <th>Ip</th>
                        <th>Navegador</th>
                        <th>Sistema</th>
                        <th>Fecha</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#if accesos?has_content>
                        <#list accesos as acceso>
                            <tr>
                                <td>${acceso.usuarioByIdUsuario.username}</td>
                                <td>${acceso.ip}</td>
                                <td>${acceso.navegador}</td>
                                <td>${acceso.sistema}</td>
                                <td>${acceso.fecha}<a href="/deleteacceso?id_acceso=${acceso.id}"><i class="fa fa-remove float-right" style="margin: 0px;margin-left: 0px;"></i></a></td>
                            </tr>
                        </#list>
                    </#if>
                    </tbody>
                </table>
            <div class="row">
                <div class="col-lg-7 col-xl-8">
                    <div class="card shadow mb-4">
                        <div class="card-header d-flex justify-content-between align-items-center">
                            <h6 class="text-primary font-weight-bold m-0">Visitas por hora</h6>
                            <div class="dropdown no-arrow"><button class="btn btn-link btn-sm dropdown-toggle" data-toggle="dropdown" aria-expanded="false" type="button"><i class="fas fa-ellipsis-v text-gray-400"></i></button>
                                <div class="dropdown-menu shadow dropdown-menu-right animated--fade-in"
                                    role="menu">
                                    <p class="text-center dropdown-header">dropdown header:</p><a class="dropdown-item" role="presentation" href="#">&nbsp;Action</a><a class="dropdown-item" role="presentation" href="#">&nbsp;Another action</a>
                                    <div class="dropdown-divider"></div><a class="dropdown-item" role="presentation" href="#">&nbsp;Something else here</a></div>
                            </div>
                        </div>
                        <div id="curve_chart" style="display: block; width: 100%; min-height: 320px;"></div>
                        <img class="img-fluid" style="height: 190px; width: 190px; margin: 0 auto" src="https://quickchart.io/qr?text=https://quickchart.io/chart?c={type:'line', data:{labels:[<#list graf_data as hora,valor>'${hora}', </#list>], datasets:[{label:'Visitas',data:[<#list graf_data as hora,valor>'${valor}',</#list>]}]}}"
                        />
                    </div>
                </div>
                <div class="col-lg-5 col-xl-4">
                    <div class="card shadow mb-4">
                        <div class="card-header d-flex justify-content-between align-items-center">
                            <h6 class="text-primary font-weight-bold m-0">Usuarios</h6>
                            <div class="dropdown no-arrow"><button class="btn btn-link btn-sm dropdown-toggle" data-toggle="dropdown" aria-expanded="false" type="button"><i class="fas fa-ellipsis-v text-gray-400"></i></button>
                                <div class="dropdown-menu shadow dropdown-menu-right animated--fade-in"
                                    role="menu">
                                    <p class="text-center dropdown-header">dropdown header:</p><a class="dropdown-item" role="presentation" href="#">&nbsp;Action</a><a class="dropdown-item" role="presentation" href="#">&nbsp;Another action</a>
                                    <div class="dropdown-divider"></div><a class="dropdown-item" role="presentation" href="#">&nbsp;Something else here</a></div>
                            </div>
                        </div>
                        <div id="donutchart" style="display: block; width: 100%; min-height: 320px;"></div>
                        <img class="img-fluid" style="height: 190px; width: 190px; margin: 0 auto" src="https://quickchart.io/qr?text=https://quickchart.io/chart?c={type:'doughnut',data:{labels:[<#list usuarios as user>'${user.username}',</#list>], datasets:[{data:[<#list usuarios as user>${user.cant},</#list>]}]}}"/>
                </div>
            </div>
        </div>
    </div>
    <footer class="bg-white sticky-footer">
        <div class="container my-auto">
            <div class="text-center my-auto copyright"><span>Copyright © Brand 2019</span></div>
        </div>
    </footer>
    </div><a class="border rounded d-inline scroll-to-top" href="#page-top"><i class="fas fa-angle-up"></i></a></div>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.3.1/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.8.0/Chart.bundle.min.js"></script>
    <script src="https://cdn.datatables.net/1.10.15/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/1.10.15/js/dataTables.bootstrap.min.js"></script>
    <script src="assets/js/bs-animation.js"></script>
    <script src="assets/js/bs-charts.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-easing/1.4.1/jquery.easing.js"></script>
    <script src="assets/js/theme.js"></script>
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">
        google.charts.load('visualization', '1', {'packages':['corechart']});
        google.charts.setOnLoadCallback(drawChart);

        function drawChart() {
            var data = google.visualization.arrayToDataTable([
                ['Fecha', 'Visitas'],
                <#list graf_data as hora,valor>
                    [new Date('${hora}'),  ${valor}],
                </#list>
            ]);

            var options = {
                title: 'Tiempo',
                curveType: 'function',
                legend: { position: 'bottom' },
            };

            var chart = new google.visualization.LineChart(document.getElementById('curve_chart'));

            chart.draw(data, options);
        }

        //donut chart
        google.charts.load('visualization', '1', {packages:["corechart"]});
        google.charts.setOnLoadCallback(drawChart2);
        function drawChart2() {
            var data = google.visualization.arrayToDataTable([
                ['Usuarios', 'Visitas'],
                <#list usuarios as user>
                    ['${user.username}', ${user.cant}],
                </#list>
            ]);

            var options = {
                //title: 'My Daily Activities',
                pieHole: 0.4,
            };

            var chart = new google.visualization.PieChart(document.getElementById('donutchart'));
            chart.draw(data, options);
        }

        $(window).resize(function(){
            drawChart();
            drawChart2();
        });
    </script>
</body>

</html>