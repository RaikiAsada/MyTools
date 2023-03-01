# frozen_string_literal: true
require 'rexml/document'

class JenkinsBuild
  attr_reader :createDate
  def initialize(jobPath, number, createDate)
    @jobPath = jobPath
    @number = number
    @createDate = createDate
  end
  def get_brunch
    doc = read_buildXml
    return doc.elements['build/actions/hudson.model.ParametersAction/parameters/net.uaznia.lukanus.hudson.plugins.gitparameter.GitParameterValue/value'].text
  end
  def success?
    result = read_result
    return result.eql?('SUCCESS')
  end
  protected def get_build_dir_path
    return "#{@jobPath}\\builds\\#{@number}"
  end
  private def read_result
    doc = read_buildXml
    return doc.elements['build/result'].text
  end

  private def read_buildXml
    Dir.chdir(get_build_dir_path)
    return REXML::Document.new File.new("build.xml")
  end

end
