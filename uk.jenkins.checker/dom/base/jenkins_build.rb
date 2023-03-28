# frozen_string_literal: true
require './dom/base/build_xml.rb'
require './dom/base/parameter_type.rb'

class JenkinsBuild
  attr_reader :number
  def self.builds_path(job)
    return File.join(job.getJobPath, 'builds')
  end
  def initialize(jobPath, number)
    @number = number.to_i
    @buildXml = BuildXml.new(jobPath, number)
  end
  def get_brunch
    return @buildXml.get_parameter(ParameterType::JENKINS_GIT_PARAM, 'BRANCH')
  end

  def success?
    result = @buildXml.get_result
    return result.eql?('SUCCESS')
  end
end
