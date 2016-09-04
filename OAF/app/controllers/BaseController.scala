package controllers

import jp.t2v.lab.play2.auth.AuthElement
import play.api.mvc.Controller

class BaseController extends Controller with AuthElement with AuthConfigImpl {

}
